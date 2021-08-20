package br.com.desafio

import io.grpc.stub.StreamObserver
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class RegistrarChavePixEndpoint(
    @Inject private val service: NovaChavePixService,
) : PixServiceGrpc.PixServiceImplBase() {

    override fun registraChavePix(
        request: RegistraPixRequest,
        responseObserver: StreamObserver<RegistraPixResponse>
    ) {
        val novaChave = request.toModel()
        val chaveCriada = service.registra(novaChave)

        responseObserver.onNext(
            RegistraPixResponse.newBuilder()
                .setClienteId(chaveCriada.clienteId.toString())
                .setPixId(chaveCriada.id.toString())
                .build()
        )
        responseObserver.onCompleted()
    }

}

fun RegistraPixRequest.toModel(): NovaChavePix {
    return NovaChavePix(
        clienteId = clienteId, tipoDeChave = when (tipoChave) {
            TipoChave.CHAVE_INVALIDA -> null
            else -> TipoChave.valueOf(tipoChave.name)
        }!!, chave = chave, tipoDeConta = when (tipoConta) {
            TipoConta.CONTA_INVALIDA -> null
            else -> TipoConta.valueOf(tipoConta.name)
        }
    )
}
