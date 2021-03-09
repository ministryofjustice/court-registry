package uk.gov.justice.digital.hmpps.courtregister.service

import com.amazonaws.services.sqs.AmazonSQSAsync
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AuditService(
  awsSqsClient: AmazonSQSAsync,
  @Value("\${sqs.queue.name}") private val queueName: String,
  @Value("\${spring.application.name}")
  private val serviceName: String


) {
  private val auditMessagingTemplate: QueueMessagingTemplate =
    QueueMessagingTemplate(awsSqsClient)

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
    val gson: Gson = GsonBuilder().create()
  }

  fun sendAuditEvent(what: String, details: Any) {
    val auditEvent = AuditEvent(what = what, who = "booby", service = serviceName, details = gson.toJson(details))
    log.debug("Audit {} ", auditEvent)
    auditMessagingTemplate.send(
      queueName,
      MessageBuilder.withPayload(gson.toJson(auditEvent)).build()
    )
  }
}

data class AuditEvent(
  val what: String,
  val `when`: LocalDateTime = LocalDateTime.now(),
  val who: String,
  val service: String,
  val details: String? = null,
)
