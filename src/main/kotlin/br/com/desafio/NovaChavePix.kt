package br.com.desafio

import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Introspected
data class NovaChavePix(

    @field:NotBlank
    val clienteId: String?,

    @field:NotNull
    val tipoDeChave: TipoChave,

    @field:Size(max = 77)
    val chave: String?,

    @field:NotNull
    val tipoDeConta: TipoConta?
) {
    fun toModel(conta: ContaAssociada): ChavePix {
        return ChavePix(
            clienteId = UUID.fromString(this.clienteId),
            tipoDeChave = TipoChave.valueOf(this.tipoDeChave!!.name),
            chave = if (this.tipoDeChave == TipoChave.ALEATORIA) UUID.randomUUID().toString() else this.chave!!,
            tipoDeConta = TipoConta.valueOf(this.tipoDeConta!!.name),
            conta = conta
        )
    }
}
