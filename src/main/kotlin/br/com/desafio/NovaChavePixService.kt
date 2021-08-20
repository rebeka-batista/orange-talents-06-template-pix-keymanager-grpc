package br.com.desafio

import io.micronaut.validation.Validated
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import javax.transaction.Transactional

@Validated
@Singleton
class NovaChavePixService(
    @Inject val repository: ChaveRepository,
    @Inject val itauClient: ItauClient,
) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun registra(novaChave: NovaChavePix): ChavePix {
        if (repository.existsByChave(novaChave.chave))
            throw IllegalArgumentException("Chave pix'${novaChave}' existente")

        val response = itauClient.consultaCliente(novaChave.clienteId!!, novaChave.tipoDeConta!!.name)
        val conta = response.body()?.toModel() ?: throw IllegalArgumentException("Cliente não encontrado no Itaú")


        val chave = novaChave.toModel(conta)
        repository.save(chave)

        return chave
    }
}
