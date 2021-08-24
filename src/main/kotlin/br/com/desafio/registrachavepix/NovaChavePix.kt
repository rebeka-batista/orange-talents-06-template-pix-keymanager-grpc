package br.com.desafio.registrachavepix


import br.com.desafio.TipoConta
import br.com.desafio.validator.ValidaChavePix
import br.com.desafio.validator.ValidaUUID
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Introspected
@ValidaChavePix
data class NovaChavePix(

    @ValidaUUID
    @field:NotBlank
    val clienteId: String?,

    @field:NotNull
    val tipoDeChave: TipoChavePix,

    @field:Size(max = 77)
    val chave: String?,

    @field:NotNull
    val tipoDeConta: TipoConta
) {
    fun toModel(conta: ContaAssociada): ChavePix {
        return ChavePix(
            clienteId = UUID.fromString(this.clienteId),
            tipoDeChave = TipoChavePix.valueOf(this.tipoDeChave!!.name),
            chave = if (this.tipoDeChave == TipoChavePix.ALEATORIA) UUID.randomUUID().toString() else this.chave!!,
            tipoDeConta = TipoConta.valueOf(this.tipoDeConta!!.name),
            conta = conta
        )
    }
}
