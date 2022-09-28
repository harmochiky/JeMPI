
TOPIC_PATIENT_STAGING_01="JeMPI-patient-staging-01"
TOPIC_PATIENT_CONTROLLER="JeMPI-patient-controller"
TOPIC_PATIENT_EM="JeMPI-patient-em"
TOPIC_PATIENT_LINKER="JeMPI-patient-linker"
TOPIC_MU_LINKER="JeMPI-mu-linker"
TOPIC_JOURNAL="JeMPI-journal"
TOPIC_NOTIFICATIONS="JeMPI-notifications"

declare -a TOPICS=(
  $TOPIC_PATIENT_STAGING_01
  $TOPIC_PATIENT_CONTROLLER
  $TOPIC_PATIENT_EM
  $TOPIC_PATIENT_LINKER
  $TOPIC_MU_LINKER
  $TOPIC_JOURNAL
  $TOPIC_NOTIFICATIONS
)

declare -A PARTITIONS
PARTITIONS[$TOPIC_PATIENT_STAGING_01]=1
PARTITIONS[$TOPIC_PATIENT_CONTROLLER]=1
PARTITIONS[$TOPIC_PATIENT_EM]=1
PARTITIONS[$TOPIC_PATIENT_LINKER]=1
PARTITIONS[$TOPIC_MU_LINKER]=1
PARTITIONS[$TOPIC_JOURNAL]=1
PARTITIONS[$TOPIC_NOTIFICATIONS]=1
  
declare -A REPLICATION
REPLICATION[$TOPIC_PATIENT_STAGING_01]=2
REPLICATION[$TOPIC_PATIENT_CONTROLLER]=2
REPLICATION[$TOPIC_PATIENT_EM]=2
REPLICATION[$TOPIC_PATIENT_LINKER]=2
REPLICATION[$TOPIC_MU_LINKER]=2
REPLICATION[$TOPIC_JOURNAL]=2
REPLICATION[$TOPIC_NOTIFICATIONS]=2

declare -A RETENTION_MS
RETENTION_MS[$TOPIC_PATIENT_STAGING_01]=`echo "1*24*60*60*1000" | bc`
RETENTION_MS[$TOPIC_PATIENT_CONTROLLER]=`echo "1*24*60*60*1000" | bc`
RETENTION_MS[$TOPIC_PATIENT_EM]=`echo "1*24*60*60*1000" | bc`
RETENTION_MS[$TOPIC_PATIENT_LINKER]=`echo "1*24*60*60*1000" | bc`
RETENTION_MS[$TOPIC_MU_LINKER]=`echo "1*24*60*60*1000" | bc`
RETENTION_MS[$TOPIC_JOURNAL]=`echo "1*24*60*60*1000" | bc`
RETENTION_MS[$TOPIC_NOTIFICATIONS]=`echo "1*24*60*60*1000" | bc`

declare -A SEGMENT_BYTES
SEGMENT_BYTES[$TOPIC_PATIENT_STAGING_01]=`echo "4*1024*1024" | bc`
SEGMENT_BYTES[$TOPIC_PATIENT_CONTROLLER]=`echo "4*1024*1024" | bc`
SEGMENT_BYTES[$TOPIC_PATIENT_EM]=`echo "4*1024*1024" | bc`
SEGMENT_BYTES[$TOPIC_PATIENT_LINKER]=`echo "4*1024*1024" | bc`
SEGMENT_BYTES[$TOPIC_MU_LINKER]=`echo "4*1024*1024" | bc`
SEGMENT_BYTES[$TOPIC_JOURNAL]=`echo "4*1024*1024" | bc`
SEGMENT_BYTES[$TOPIC_NOTIFICATIONS]=`echo "4*1024*1024" | bc`
