package uk.gov.justice.digital.hmpps.courtregister.jpa

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.courtregister.jpa.Court.CourtType.OTHER
import javax.persistence.Entity
import javax.persistence.EnumType.STRING
import javax.persistence.Enumerated
import javax.persistence.Id

@Repository
interface CourtRepository : CrudRepository<Court, String> {
  fun findByActiveOrderById(active: Boolean): List<Court>
}

@Entity
data class Court(
  @Id
  val id: String,
  var courtName: String,
  var courtDescription: String?,
  @Enumerated(STRING)
  var courtType: CourtType = OTHER,
  var active: Boolean
) {
  enum class CourtType {
    MAGISTRATES, CROWN, COUNTY, YOUTH, OTHER, SHERRIFS_SCOTTISH, DISTRICT_SCOTTISH, HIGH_COURT_SCOTTISH, ASYLUM_IMMIGRATION, IMMIGRATION, COURTS_MARTIAL, OUTSIDE_ENG_WALES, APPEAL
  }
}
