package ch.usi.inf.sa4.sphinx.controller;


import ch.usi.inf.sa4.sphinx.misc.*;
import ch.usi.inf.sa4.sphinx.model.Scene;
import ch.usi.inf.sa4.sphinx.model.Serialiser;
import ch.usi.inf.sa4.sphinx.service.DeviceService;
import ch.usi.inf.sa4.sphinx.service.RoomService;
import ch.usi.inf.sa4.sphinx.service.UserService;
import ch.usi.inf.sa4.sphinx.service.SceneService;
import ch.usi.inf.sa4.sphinx.view.SerialisableScene;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@Api(value = "Scene endpoint", description = "Scene Controller")
@CrossOrigin(origins = {"http://localhost:3000", "https://smarthut.xyz"})
@RestController
@RequestMapping("/scene")

public class SceneController {


        @Autowired
        UserService userService;
        @Autowired
        DeviceService deviceService;
        @Autowired
        RoomService roomService;
        @Autowired
        SceneService sceneService;
        @Autowired
        Serialiser serialiser;

        /**
         * Returns a list of all rooms which belong to the given user.
         *
         * @param sessionToken session token of the user
         * @param username     the username of the user
         * @return a ResponseEntity with the array of rooms owned by the user
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

            check(sessionToken, username,null);

            return ResponseEntity.ok(Serialiser.serialiseScenes(userService.getPopulatedScenes(username)));
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
            if (!userService.validSession(username, sessionToken)) {
                throw new UnauthorizedException("Invalid credentials");
            }


        }
    }
