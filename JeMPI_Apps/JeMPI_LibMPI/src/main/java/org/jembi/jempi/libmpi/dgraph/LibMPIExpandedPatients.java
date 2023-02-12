package org.jembi.jempi.libmpi.dgraph;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jembi.jempi.shared.utils.LibMPIPagination;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
record LibMPIExpandedPatients(
      @JsonProperty("all") List<CustomLibMPIExpandedPatientRecord> all,
      @JsonProperty("pagination") List<LibMPIPagination> pagination) {

   public LibMPIExpandedPatients(@JsonProperty("all") List<CustomLibMPIExpandedPatientRecord> all) {
      this(all, List.of(new LibMPIPagination(all.size())));
   }

}
