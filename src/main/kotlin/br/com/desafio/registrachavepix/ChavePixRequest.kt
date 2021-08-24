package br.com.desafio.registrachavepix

import br.com.desafio.RegistraPixRequest
import br.com.desafio.TipoChave
import br.com.desafio.TipoConta


fun RegistraPixRequest.toModel(): NovaChavePix {
    return NovaChavePix(
        clienteId = clienteId,
        tipoDeChave = when (tipoChave) {
            TipoChave.CHAVE_INVALIDA -> throw
            IllegalArgumentException("Tipo de Chave desconhecida")
            else -> br.com.desafio.registrachavepix.TipoChavePix.valueOf(tipoChave.name)
        }, chave = chave, tipoDeConta = when (tipoConta) {
            TipoConta.CONTA_INVALIDA-> throw
            IllegalArgumentException("Tipo de Conta desconhecida")
            else -> TipoConta.valueOf(tipoConta.name)
        }
    )
}
