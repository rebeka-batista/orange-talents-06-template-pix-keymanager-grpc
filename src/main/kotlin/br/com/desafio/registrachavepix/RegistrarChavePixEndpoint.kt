package br.com.desafio.registrachavepix

import br.com.desafio.PixServiceGrpc
import br.com.desafio.RegistraPixRequest
import br.com.desafio.RegistraPixResponse
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegistrarChavePixEndpoint(
    @Inject private var service: NovaChavePixService,
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
