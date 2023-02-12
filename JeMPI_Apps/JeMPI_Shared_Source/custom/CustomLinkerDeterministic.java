package org.jembi.jempi.linker;

import org.apache.commons.lang3.StringUtils;
import org.jembi.jempi.shared.models.CustomDemographicData;

class CustomLinkerDeterministic {

   private CustomLinkerDeterministic() {}

   private static boolean isMatch(
         final String left,
         final String right) {
      return StringUtils.isNotBlank(left) && StringUtils.equals(left, right);
   }

   static boolean deterministicMatch(
         final CustomDemographicData goldenRecord,
         final CustomDemographicData patient) {
      final var givenName_l = goldenRecord.givenName();
      final var givenName_r = patient.givenName();
      final var familyName_l = goldenRecord.familyName();
      final var familyName_r = patient.familyName();
      final var phoneNumber_l = goldenRecord.phoneNumber();
      final var phoneNumber_r = patient.phoneNumber();
      final var nationalId_l = goldenRecord.nationalId();
      final var nationalId_r = patient.nationalId();
      return (isMatch(nationalId_l, nationalId_r) || (isMatch(givenName_l, givenName_r) && isMatch(familyName_l,
                                                                                                   familyName_r) && isMatch(
            phoneNumber_l,
            phoneNumber_r)));
   }

}
