package org.jembi.jempi.libmpi;

public sealed interface MpiServiceError extends MpiGeneralError {

   record InteractionIdDoesNotExistError(
         String error,
         String interactionID) implements MpiServiceError {}

   record GoldenIdDoesNotExistError(
         String error,
         String goldenID) implements MpiServiceError {}

   record GoldenIdInteractionConflictError(
         String error,
         String goldenID,
         String interactionID) implements MpiServiceError {}

   record DeletePredicateError(
         String uid,
         String predicate) implements MpiServiceError {}

   record GeneralError(String error) implements MpiServiceError {}

   record CandidatesNotFoundError(
         String error,
         String interactionID) implements MpiServiceError {}

}
