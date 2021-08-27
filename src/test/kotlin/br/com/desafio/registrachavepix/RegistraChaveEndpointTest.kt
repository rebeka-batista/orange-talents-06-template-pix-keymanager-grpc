package br.com.desafio.registrachavepix

import br.com.desafio.PixServiceGrpc
import br.com.desafio.RegistraPixRequest
import br.com.desafio.TipoChave
import br.com.desafio.TipoConta
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.http.HttpResponse
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.util.*

@MicronautTest(transactional = false)
internal class RegistraChaveEndpointTest(

    val repository: ChaveRepository,
    val grpcClient: PixServiceGrpc.PixServiceBlockingStub,
    val itauClient: ItauClient

) {

    companion object {
        val Client_ID = UUID.randomUUID()
    }

    @BeforeEach
    fun setup() {
        repository.deleteAll()
    }

    @MockBean(ItauClient::class)
    fun itauClient(): ItauClient? {
        return Mockito.mock(ItauClient::class.java)
    }

    @Factory
    class Clients {
        @Bean
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): PixServiceGrpc.PixServiceBlockingStub {
            return PixServiceGrpc.newBlockingStub(channel)
        }
    }

    private fun dadosDaContaResponse(): DadosDaContaResponse {
        return DadosDaContaResponse(
            tipo = "CONTA_CORRENTE",
            instituicao = InstituicaoResponse("UNIBANCO ITAU SA", "ITAU_UNIBANCO_ISBP"),
            agencia = "1218",
            numero = "291900",
            titular = TitularResponse("RAFAEL M C PONTE", "41531952860")
        )
    }


    @Test
    fun `deve registrar chave pix`() {
        `when`(itauClient.consultaCliente(idCliente = UUID.randomUUID().toString(), tipo = "CONTA_CORRENTE"))
            .thenReturn(HttpResponse.ok())
        repository.save(
            ChavePix(
                tipoDeChave = TipoChavePix.CPF,
                chave = "02467781054",
                clienteId = UUID.randomUUID(),
                tipoDeConta = TipoConta.CONTA_CORRENTE,
                conta = dadosDaContaResponse().toModel()
            )
        )
        assertEquals(1, repository.count())
    }


    @Test
    fun `nao deve registrar chave pix existente`() {
        repository.save(
            ChavePix(
                tipoDeChave = br.com.desafio.registrachavepix.TipoChavePix.CPF,
                chave = "41531952860",
                clienteId = Client_ID,
                tipoDeConta = TipoConta.CONTA_CORRENTE,
                conta = dadosDaContaResponse().toModel()
            )
        )
        val testKey = repository.existsByChave("41531952860")
        assertTrue(testKey, Status.ALREADY_EXISTS.toString())
    }


    @Test
    fun `nao deve registrar chave pix que nao encontrar dados da conta no client`() {

        `when`(itauClient.consultaCliente(idCliente = Client_ID.toString(), tipo = "CONTA_CORRENTE"))
            .thenReturn(HttpResponse.notFound())

        val thrown = assertThrows<StatusRuntimeException> {
            grpcClient.registraChavePix(
                RegistraPixRequest.newBuilder()
                    .setClienteId(Client_ID.toString())
                    .setTipoChave(TipoChave.EMAIL)
                    .setChave("rebekabatista@outlook.com")
                    .setTipoConta(TipoConta.CONTA_CORRENTE)
                    .build()
            )
        }
        with(thrown) {
            assertEquals(Status.FAILED_PRECONDITION.code, status.code)
            assertEquals("Cliente não encontrado no Itaú", status.description)
        }
    }

}

