package com.bory.eventsourcingtutorial.client.infrastructure.eventlistener

import com.bory.eventsourcingtutorial.core.infrastructure.annotations.DomainEventListener
import org.springframework.transaction.annotation.Transactional

@Transactional
@DomainEventListener
class ClientEventListener