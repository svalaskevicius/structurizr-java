package com.structurizr.dsl;

import com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy;
import com.structurizr.model.CreateImpliedRelationshipsUnlessSameRelationshipExistsStrategy;
import com.structurizr.model.CreateImpliedRelationshipsUnlessParentRelationshipExistsStrategy;
import com.structurizr.model.DefaultImpliedRelationshipsStrategy;

import java.util.ArrayList;
import java.util.List;

final class ImpliedRelationshipsParser extends AbstractParser {

    private static final String GRAMMAR = "!impliedRelationships <true|false|same|parent>";

    private static final int FLAG_INDEX = 1;
    private static final String FALSE = "false";
    private static final String SAME = "same";
    private static final String PARENT = "parent";

    void parse(DslContext context, Tokens tokens) {
        // impliedRelationships <true|false>

        if (tokens.hasMoreThan(FLAG_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(FLAG_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        if (tokens.get(FLAG_INDEX).equalsIgnoreCase(FALSE)) {
            context.getWorkspace().getModel().setImpliedRelationshipsStrategy(new DefaultImpliedRelationshipsStrategy());

        } else if (tokens.get(FLAG_INDEX).equalsIgnoreCase(SAME)) {
            context.getWorkspace().getModel().setImpliedRelationshipsStrategy(new CreateImpliedRelationshipsUnlessSameRelationshipExistsStrategy());
        } else if (tokens.get(FLAG_INDEX).equalsIgnoreCase("parent")) {
            context.getWorkspace().getModel().setImpliedRelationshipsStrategy(
              new CreateImpliedRelationshipsUnlessParentRelationshipExistsStrategy()
            );
        } else {
            context.getWorkspace().getModel().setImpliedRelationshipsStrategy(new CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy());
        }
    }

}
