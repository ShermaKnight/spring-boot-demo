package org.example.http;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.FI;

import org.example.http.UserMessages.CreateUserMessage;
import org.example.http.UserMessages.GetUserMessage;
import org.example.http.UserMessages.ActionPerformed;

public class UserActor extends AbstractActor {

    private UserService userService = new UserService();

    static Props props() {
        return Props.create(UserActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CreateUserMessage.class, handleCreateUser())
                .match(GetUserMessage.class, handleGetUser())
                .build();
    }

    private FI.UnitApply<CreateUserMessage> handleCreateUser() {
        return createUserMessageMessage -> {
            userService.createUser(createUserMessageMessage.getUser());
            sender().tell(new ActionPerformed(String.format("User %s created.", createUserMessageMessage.getUser().getName())), getSelf());
        };
    }

    private FI.UnitApply<GetUserMessage> handleGetUser() {
        return getUserMessageMessage -> {
            sender().tell(userService.getUser(getUserMessageMessage.getUserId()), getSelf());
        };
    }
}
