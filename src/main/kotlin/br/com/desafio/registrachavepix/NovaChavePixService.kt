package br.com.desafio.registrachavepix

import br.com.desafio.validator.ChavePixExistenteException
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NovaChavePixService(
    @Inject val repository: ChaveRepository,
    @Inject val itauClient: ItauClient,
) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    fun registra(novaChave: NovaChavePix): ChavePix {
        if (repository.existsByChave(novaChave.chave)) {
            throw  ChavePixExistenteException("Chave pix já existente")
        }

        val response = itauClient.consultaCliente(novaChave.clienteId!!, novaChave.tipoDeConta!!.name)
        val conta = response.body()?.toModel() ?: throw IllegalStateException("Cliente não encontrado no Itaú")

        val chave = novaChave.toModel(conta)
        repository.save(chave)
        return chave
    }
}
