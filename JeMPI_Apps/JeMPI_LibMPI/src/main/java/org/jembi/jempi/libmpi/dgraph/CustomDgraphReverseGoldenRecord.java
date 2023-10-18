package org.jembi.jempi.libmpi.dgraph;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jembi.jempi.shared.models.GoldenRecordWithScore;
import org.jembi.jempi.shared.models.CustomUniqueGoldenRecordData;
import org.jembi.jempi.shared.models.CustomDemographicData;
import org.jembi.jempi.shared.models.GoldenRecord;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
record CustomDgraphReverseGoldenRecord(
      @JsonProperty("uid") String goldenId,
      @JsonProperty("GoldenRecord.source_id") List<DgraphSourceId> sourceId,
      @JsonProperty(CustomDgraphConstants.PREDICATE_GOLDEN_RECORD_AUX_DATE_CREATED) java.time.LocalDateTime auxDateCreated,
      @JsonProperty(CustomDgraphConstants.PREDICATE_GOLDEN_RECORD_AUX_AUTO_UPDATE_ENABLED) Boolean auxAutoUpdateEnabled,
      @JsonProperty(CustomDgraphConstants.PREDICATE_GOLDEN_RECORD_AUX_ID) String auxId,
      @JsonProperty(CustomDgraphConstants.PREDICATE_GOLDEN_RECORD_GIVEN_NAME) String givenName,
      @JsonProperty(CustomDgraphConstants.PREDICATE_GOLDEN_RECORD_FAMILY_NAME) String familyName,
      @JsonProperty(CustomDgraphConstants.PREDICATE_GOLDEN_RECORD_GENDER) String gender,
      @JsonProperty(CustomDgraphConstants.PREDICATE_GOLDEN_RECORD_DOB) String dob,
      @JsonProperty(CustomDgraphConstants.PREDICATE_GOLDEN_RECORD_CITY) String city,
      @JsonProperty(CustomDgraphConstants.PREDICATE_GOLDEN_RECORD_PHONE_NUMBER_HOME) String phoneNumberHome,
      @JsonProperty(CustomDgraphConstants.PREDICATE_GOLDEN_RECORD_PHONE_NUMBER_MOBILE) String phoneNumberMobile,
      @JsonProperty(CustomDgraphConstants.PREDICATE_GOLDEN_RECORD_PHN) String phn,
      @JsonProperty(CustomDgraphConstants.PREDICATE_GOLDEN_RECORD_NIC) String nic,
      @JsonProperty(CustomDgraphConstants.PREDICATE_GOLDEN_RECORD_PPN) String ppn,
      @JsonProperty(CustomDgraphConstants.PREDICATE_GOLDEN_RECORD_SCN) String scn,
      @JsonProperty(CustomDgraphConstants.PREDICATE_GOLDEN_RECORD_DL) String dl,
      @JsonProperty("~GoldenRecord.interactions|score") Float score) {

   GoldenRecord toGoldenRecord() {
      return new GoldenRecord(this.goldenId(),
                              this.sourceId() != null
                                    ? this.sourceId().stream().map(DgraphSourceId::toSourceId).toList()
                                    : List.of(),
                              new CustomUniqueGoldenRecordData(this.auxDateCreated(),
                                                               this.auxAutoUpdateEnabled(),
                                                               this.auxId()),
                              new CustomDemographicData(this.givenName(),
                                                        this.familyName(),
                                                        this.gender(),
                                                        this.dob(),
                                                        this.city(),
                                                        this.phoneNumberHome(),
                                                        this.phoneNumberMobile(),
                                                        this.phn(),
                                                        this.nic(),
                                                        this.ppn(),
                                                        this.scn(),
                                                        this.dl()));
   }

   GoldenRecordWithScore toGoldenRecordWithScore() {
      return new GoldenRecordWithScore(toGoldenRecord(), score);
   }

}

