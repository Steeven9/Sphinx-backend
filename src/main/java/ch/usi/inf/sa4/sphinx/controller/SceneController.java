package ch.usi.inf.sa4.sphinx.controller;


import ch.usi.inf.sa4.sphinx.misc.BadRequestException;
import ch.usi.inf.sa4.sphinx.misc.NotFoundException;
import ch.usi.inf.sa4.sphinx.model.sceneEffects.Scene;
import ch.usi.inf.sa4.sphinx.model.sceneEffects.SceneType;
import ch.usi.inf.sa4.sphinx.service.DeviceService;
import ch.usi.inf.sa4.sphinx.service.RoomService;
import ch.usi.inf.sa4.sphinx.service.SceneService;
import ch.usi.inf.sa4.sphinx.service.UserService;
import ch.usi.inf.sa4.sphinx.view.SerialisableScene;
import ch.usi.inf.sa4.sphinx.view.SerialisableSceneEffect;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@Api(value = "Scene endpoint", description = "Scene Controller")
@CrossOrigin(origins = {"http://localhost:3000/*", "http://localhost:3000", "https://smarthut.xyz"})
@RestController
@RequestMapping("/scenes")
public class SceneController {

    @Autowired
    UserService userService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    RoomService roomService;
    @Autowired
    SceneService sceneService;

    /**
     * Returns a list of all scenes which belong to the given user.
     *
     * @param sessionToken session token of the user
     * @param username     the username of the user
     * @return a ResponseEntity with the array of scenes owned by the user
     * @see Scene
     * @see SerialisableScene
     */
    @GetMapping({"", "/"})
    @ApiOperation("Returns all the scenes owned by the User")
    @ApiResponses(
            {
                    @ApiResponse(code = 404, message = "if the User is not found"),
            }
    )
    public ResponseEntity<List<SerialisableScene>> getAllScenes(@NotNull @RequestHeader("session-token") final String sessionToken,
                                                                @NotNull @RequestHeader("user") final String username) {

        check(sessionToken, username, null);
        return ResponseEntity.ok(sceneService.getOwnedScenes(username)
                .stream()
                .map(Scene::serialise)
                .collect(Collectors.toList()));
    }


    /**
     * Creates a new room.
     *
     * @param sessionToken session token of the user
     * @param username     the username of the user
     * @return a new room
     */
    @GetMapping("/{sceneId}")
    @ApiOperation("Modifies a Scene")
    public ResponseEntity<SerialisableScene> getSceneById(@NotNull @PathVariable final Integer sceneId,
                                                          @NotNull @RequestHeader("session-token") final String sessionToken,
                                                          @NotNull @RequestHeader("user") final String username) {
        check(sessionToken, username, null, sceneId);

        final Scene storageScene = sceneService.get(sceneId).orElseThrow(() -> new NotFoundException("No scenes found"));
        return ResponseEntity.ok().body(storageScene.serialise());
    }


    /**
     * Creates a new Scene.
     *
     * @param sessionToken      session token of the user
     * @param username          the username of the user
     * @param serialisableScene a new room
     * @param errors            in case error occur
     * @return a new scene
     */
    @PostMapping({"", "/"})
    @ApiOperation("Creates a Scene")
    public ResponseEntity<SerialisableScene> createScene(@NotBlank @RequestHeader("session-token") final String sessionToken,
                                                         @NotBlank @RequestHeader("user") final String username,
                                                         @NotNull @RequestBody final SerialisableScene serialisableScene,
                                                         final Errors errors) {

        check(sessionToken, username, errors);

        boolean owned = serialisableScene.getEffects().stream()
                .flatMap(effect -> effect.getDevices().stream())
                .allMatch(id -> userService.ownsDevice(username, id));

        if (!owned) {
            throw new NotFoundException("failed to retrive devices");
        }

        final Scene sc = sceneService.createScene(username, serialisableScene.getName(), serialisableScene.getIcon())
                .orElseThrow(() -> new BadRequestException(""));


        List<SerialisableSceneEffect> effects = serialisableScene.getEffects();


        if (effects != null) {
            effects.forEach(effect -> {
                        try {
                            sceneService.addEffect(
                                    sc.getId(),
                                    effect.getDevices(),
                                    SceneType.intToType(effect.getType()),
                                    effect.getName(),
                                    effect.getTarget());
                        } catch (IllegalArgumentException e) {
                            throw new BadRequestException("Incompatible effect type");

                        }
                    }
            );
        }

        final SerialisableScene res = sc.serialise();

        return ResponseEntity.status(201).body(res);

    }

    /**
     * Creates a new room.
     *
     * @param sessionToken      session token of the user
     * @param username          the username of the user
     * @param serialisableScene a new room
     * @param errors            in case error occur
     * @return a new room
     */
    @PutMapping("/{sceneId}")
    @ApiOperation("Modifies a Scene")
    public ResponseEntity<SerialisableScene> modifyScene(@NotNull @PathVariable final Integer sceneId,
                                                         @NotNull @RequestHeader("session-token") final String sessionToken,
                                                         @NotNull @RequestHeader("user") final String username,
                                                         @NotNull @RequestBody final SerialisableScene serialisableScene,
                                                         final Errors errors) {
        check(sessionToken, username, errors, sceneId);

        final Scene storageScene = sceneService.get(sceneId).orElseThrow(() -> new NotFoundException("No scenes found"));

        if (serialisableScene.getName() != null) {
            storageScene.setName(serialisableScene.getName());
        }

        if (serialisableScene.getIcon() != null) {
            storageScene.setIcon(serialisableScene.getIcon());
        }

        sceneService.removeEffects(sceneId);

        List<SerialisableSceneEffect> effects = serialisableScene.getEffects();
        effects.forEach(effect -> {
                    try {
                        sceneService.addEffect(
                                sceneId,
                                effect.getDevices(),
                                SceneType.intToType(effect.getType()),
                                effect.getName(),
                                effect.getTarget());
                    } catch (IllegalArgumentException e) {
                        throw new BadRequestException("Incompatible effect type");

                    }
                }
        );


        final SerialisableScene res = storageScene.serialise();
        return ResponseEntity.ok().body(res);
    }

//
    @DeleteMapping("/{sceneId}")
    @ApiOperation("Delete a Scene")
    public ResponseEntity<SerialisableScene> deleteScene(@NotNull @PathVariable final Integer sceneId,
                                                         @NotNull @RequestHeader("session-token") final String sessionToken,
                                                         @NotNull @RequestHeader("user") final String username) {

        check(sessionToken, username, null, sceneId);

        sceneService.get(sceneId).orElseThrow(() -> new NotFoundException("")).run();
        sceneService.deleteScene(sceneId);

        return ResponseEntity.noContent().build();

    }

    @PutMapping("run/{sceneId}")
    @ApiOperation("Runs a Scene")
    public ResponseEntity<SerialisableScene> runScene(@NotNull @PathVariable final Integer sceneId,
                                                      @NotNull @RequestHeader("session-token") final String sessionToken,
                                                      @NotNull @RequestHeader("user") final String username) {

        check(sessionToken, username, null, sceneId);

        sceneService.get(sceneId).orElseThrow(() -> new NotFoundException("")).run();

        return ResponseEntity.noContent().build();

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
    private void check(final String sessionToken, final String username, final Errors errors) {
        if (errors != null && errors.hasErrors()) {
            throw new BadRequestException(errors.getAllErrors().toString());
        }
        userService.validateSession(username, sessionToken);
    }


    /**
     * Checks if the request parameters are correct. Throws if they are not.
     * It will check that:
     * there are no validation errors
     * the User exists and has a valid sessionToken
     * the room with the given id belongs to the user
     *
     * @param sessionToken session token of the user
     * @param username     the username of the user
     * @param errors       in case error occur
     * @param sceneId      the id of the room
     */
    private void check(final String sessionToken, final String username, final Errors errors, final Integer sceneId) {
        check(sessionToken, username, errors);

//        if (!sceneService.isOwnedBy(username, sceneId)) throw new UnauthorizedException("You don't own this room");

    }
}
