package com.structurizr.model;

/**
 * This strategy creates implied relationships between all valid combinations of
 * the parent elements,
 * unless the same relationship already exists between them.
 */
public class CreateImpliedRelationshipsUnlessParentRelationshipExistsStrategy
        extends AbstractImpliedRelationshipsStrategy {

    @Override
    public void createImpliedRelationships(Relationship relationship) {
        Element source = relationship.getSource();
        Element destination = relationship.getDestination();

        Model model = source.getModel();

        while (source != null) {
            boolean ownedRelationshipExists = false;
            while (destination != null && !ownedRelationshipExists) {
                if (impliedRelationshipIsAllowed(source, destination)) {
                    boolean createRelationship = !source.hasEfferentRelationshipWith(destination,
                            relationship.getDescription());
                    for (Relationship r : source.getEfferentRelationshipsWith(destination)) {
                        Element rDest = r.getDestination();
                        Element rSrc = r.getSource();
                        if (r.getLinkedRelationshipId() != null) {
                            Relationship linked = model.getRelationship(r.getLinkedRelationshipId());
                            rDest = linked.getDestination();
                            rSrc = linked.getSource();
                        }
                        if (relationship.getDestination().getAncestors().contains(rDest)
                                || relationship.getSource().getAncestors().contains(rSrc)) {
                            createRelationship = false;
                            ownedRelationshipExists = true;
                        } else if (r.getLinkedRelationshipId() != null
                                && (relationship.getDestination().getDescendants().contains(rDest)
                                        || relationship.getSource().getDescendants().contains(rSrc))) {
                            createRelationship = true;
                            model.removeRelationship(r);
                        }

                    }

                    if (createRelationship) {
                        createImpliedRelationship(relationship, source, destination);
                    }
                }

                destination = destination.getParent();
            }

            destination = relationship.getDestination();
            source = source.getParent();
        }
    }

}
