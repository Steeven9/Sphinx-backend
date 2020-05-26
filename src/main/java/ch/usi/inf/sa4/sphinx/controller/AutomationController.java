package ch.usi.inf.sa4.sphinx.controller;


import ch.usi.inf.sa4.sphinx.misc.BadRequestException;
import ch.usi.inf.sa4.sphinx.misc.NotFoundException;
import ch.usi.inf.sa4.sphinx.misc.ServerErrorException;
import ch.usi.inf.sa4.sphinx.model.Automation;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.model.sceneEffects.Scene;
import ch.usi.inf.sa4.sphinx.service.*;
import ch.usi.inf.sa4.sphinx.view.SerialisableAutomation;
import ch.usi.inf.sa4.sphinx.view.SerialisableCondition;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

//import lombok.NonNull;

@CrossOrigin(origins = {"http://localhost:3000", "https://smarthut.xyz"})
@RestController
@RequestMapping("/automations")
public class AutomationController {
    @Autowired
    CouplingService couplingService;
    @Autowired
    UserService userService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    RoomService roomService;
    @Autowired
    private AutomationService automationService;
    @Autowired
    private SceneService sceneService;


    /**
     * Gets the automations owned by a User.
     *
     * @param sessionToken the session token of the User
     * @param username     the username of the User
     * @return a ResponseEntity with the ids of the automations owned by the user or
     * - 404 if not found or
     * - 401 if not authorized
     * @see User
     * @see SerialisableDevice
     * @see Device
     */
    @GetMapping({"/{automationId}"})
    @ApiOperation("Gets the automations")
    public ResponseEntity<SerialisableAutomation> getAutomation(@RequestHeader("session-token") final String sessionToken,
                                                                @RequestHeader("user") final String username,
                                                                @PathVariable Integer automationId) {


        checkRequest(username, sessionToken);

        Automation automation = automationService.findById(automationId).orElseThrow(() -> new NotFoundException(""));

        if (!automation.getUser().getUsername().equals(username)) throw new NotFoundException("");

        return ResponseEntity.ok(automation.serialise());


    }

    /**
     * Gets the automations owned by a User.
     *
     * @param sessionToken the session token of the User
     * @param username     the username of the User
     * @return a ResponseEntity with the ids of the automations owned by the user or
     * - 404 if not found or
     * - 401 if not authorized
     * @see User
     * @see SerialisableDevice
     * @see Device
     */
    @GetMapping({"", "/"})
    @ApiOperation("Gets the automations")
    public ResponseEntity<List<SerialisableAutomation>> getAutomations(@RequestHeader("session-token") final String sessionToken,
                                                                       @RequestHeader("user") final String username) {


        checkRequest(username, sessionToken);
        List<Automation> automations = automationService.findByOwner(username).orElseThrow(() -> new ServerErrorException(""));

        return ResponseEntity.ok(automations.stream().map(Automation::serialise).collect(Collectors.toList()));

    }


    /**
     * Delete the automation with the given id.
     *
     * @param sessionToken the session token of the User
     * @param username     the username of the User
     * @param
     * @return a ResponseEntity with the ids of the automations owned by the user or
     * - 404 if not found or
     * - 401 if not authorized
     * @see User
     * @see SerialisableDevice
     * @see Device
     */
    @DeleteMapping({"/{automationId}"})
    @ApiOperation("Gets the automations")
    public ResponseEntity deleteAutomations(@RequestHeader("session-token") final String sessionToken,
                                            @RequestHeader("user") final String username,
                                            @PathVariable @NotNull Integer automationId
    ) {


        checkRequest(username, sessionToken);
        final Automation automation = automationService
                .findById(automationId)
                .orElseThrow(() -> new NotFoundException("Automation not found"));

        if (!automation.getUser().getUsername().equals(username)) throw new NotFoundException("Automation not found");
        automationService.deleteAutomation(automation.getId());

        return ResponseEntity.noContent().build();

    }


//
//    Request:
//    Headers:
//    session-token: {{session_token}}
//    user: {{username}}
//    Body:
//    name: {{automationName}}
//    scenes: [...{scene}],
//    triggers: [...{
//        source: {{deviceId}},
//        type: {{conditionType}}
//        value:{{effectValue}},
//    }]
//    conditions: [...{null ||
//            source: {{deviceId}},
//        type: {{conditionType}}
//        value:{{effectValue}},
//    }]


    /**
     * modifies the Automation with the given automationId to conform to the fields in the given SerialisableAutomation,
     * if the user is authenticating with the correct user/session-token pair
     *
     * @param automationId the id of the device to be modified
     * @param automation   an object representing the device to modify
     * @param sessionToken the session token of the user to authenticate as
     * @param username     the username of the user to authenticate as
     * @param errors       validation errors
     * @return a ResponseEntity with the data of the modified Automation and status code 200 if operation is successful or
     * - 400 if bad request or
     * - 404 if no such automation exist or
     * - 401 if authentication fails or
     * - 500 in case of a server error
     * @see SerialisableAutomation
     * @see Automation
     */
    @PutMapping("/{automationId}")
    @ApiOperation("Modifies an Automation")
    public ResponseEntity<SerialisableAutomation> modifyAutomation(@NotNull @PathVariable final Integer automationId,
                                                                   @RequestHeader("session-token") final String sessionToken,
                                                                   @RequestHeader("user") final String username,
                                                                   @NotBlank @RequestBody final SerialisableAutomation automation,
                                                                   final Errors errors) {


        checkRequest(username, sessionToken, errors);
        Automation storageAutomation = automationService.findById(automationId).orElseThrow(() -> new NotFoundException(""));
        if (!storageAutomation.getUser().getUsername().equals(username)) throw new NotFoundException("");

        if (automation.getScenes() != null) {
            List<Integer> putScenes = automation.getScenes(); //gets modified, dont use later or make copy of it
            storageAutomation.getScenes().forEach(scene -> {
                if (!putScenes.contains(scene.getId())) {
                    automationService.removeScene(storageAutomation.getId(), scene.getId());
                    putScenes.remove(scene.getId()); //THIS WONT WORK IF THE TYPE OF StorableE.getId() is made primitive
                }
            });
            automationService.addScenesIfBelongsTo(username, automationId, putScenes);
        }


        if (automation.getConditions() != null) {
            automationService.removeConditions(automationId);

            automation.getConditions().forEach(condition ->
                    automationService.addCondition(automationId,
                            condition.getSource(),
                            condition.getEventType(),
                            condition.getTarget())
            );
        }

        if (automation.getTriggers() != null) {
            automationService.removeTriggers(automationId);

            automation.getTriggers().forEach(trigger ->
                    automationService.addTrigger(automationId,
                            trigger.getSource(),
                            trigger.getEventType(),
                            trigger.getTarget())
            );
        }


        return ResponseEntity.ok(storageAutomation.serialise());
    }


    /**
     * Checks if the request parameters are correct. Throws if they are not.
     * It will check that:
     * there are no validation errors
     * the User exists and has a valid sessionToken
     *
     * @param sessionToken session token of the user
     * @param username     the username of the user
     * @param errors       in case error occur
     */
    private void checkRequest(final String username, final String sessionToken, final Errors errors) {
        if (errors != null && errors.hasErrors()) {
            throw new BadRequestException(errors.getAllErrors().toString());
        }
        checkRequest(username, sessionToken);
    }

    /**
     * Creates a new Automation given a SerialisableAutomation. The automation will contain all the triggers,
     * conditions and scenes specified in the
     *
     * @param automation   data of the automation to be created
     * @param sessionToken sessionToken of the user
     * @param username     username of the user
     * @param errors       errors in validating the fields
     * @return a ResponseEntity with the data of the newly created automation (201), or
     * - 400 if bad request or
     * - 401 if not authorized or
     * - 500 if an internal server error occurred
     */
    @PostMapping({"", "/"})
    @ApiOperation("Creates an automation")
    public ResponseEntity<SerialisableAutomation> createAutomation(@RequestHeader("session-token") final String sessionToken,
                                                                   @RequestHeader("user") final String username,
                                                                   @NotNull @RequestBody final SerialisableAutomation automation,
                                                                   final Errors errors) {

        checkRequest(username, sessionToken, errors);
        Automation storageAutomation = automationService.createAutomation(username)
                .orElseThrow(() -> new ServerErrorException("Failed to create the coupling"));
        Integer autoId = storageAutomation.getId();

        List<Integer> sceneIds = automation.getScenes();
        List<SerialisableCondition> conditions = automation.getConditions();
        List<SerialisableCondition> triggers = automation.getTriggers();


        sceneIds.forEach(id -> {
            Scene scene = sceneService.get(id).orElseThrow(() -> new NotFoundException("Scene not found"));
            if (!scene.getUser().getUsername().equals(username)) throw new NotFoundException("Scene not found");
            automationService.addScene(autoId, id);
        });

        conditions.forEach(condition ->
                automationService.addCondition(autoId, condition.getSource(), condition.getEventType(), condition.getTarget())
        );


        triggers.forEach(trigger ->
                automationService.addTrigger(autoId, trigger.getSource(), trigger.getEventType(), trigger.getTarget())
        );


        return ResponseEntity.status(HttpStatus.CREATED).body(storageAutomation.serialise());

    }


    /**
     * Checks if the request parameters are correct. Throws if they are not.
     * It will check that:
     * the User exists and has a valid sessionToken
     *
     * @param sessionToken session token of the user
     * @param username     the username of the user
     */
    private void checkRequest(final String username, final String sessionToken) {
        userService.validateSession(username, sessionToken);
    }
}



