package br.com.desafio.registrachavepix

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface ChaveRepository : JpaRepository<ChavePix, UUID> {

    fun existsByChave(chave: String?): Boolean
}
