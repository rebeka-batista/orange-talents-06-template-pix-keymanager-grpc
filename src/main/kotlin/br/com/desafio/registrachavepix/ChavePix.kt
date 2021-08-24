package br.com.desafio.registrachavepix

import br.com.desafio.TipoConta
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.NotNull

@Entity
@Table(
    uniqueConstraints = [UniqueConstraint(
        name = "uk_chave_pix",
        columnNames = ["chave"]
    )]
)

class ChavePix(

    @field:NotNull
    @Column(nullable = false)
    val clienteId: UUID?,

    @field:NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val tipoDeChave: TipoChavePix,

    @field: NotNull
    @Column(unique = true, nullable = false)
    var chave: String?,

    @field: NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val tipoDeConta: TipoConta?,

    @field:Valid
    @Embedded
    val conta: ContaAssociada?
) {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    var id: UUID? = null


    @Column(nullable = false)
    val criadaEm: LocalDateTime = LocalDateTime.now()


    override fun toString(): String {
        return "ChavePix(clienteId=$clienteId, tipoDeChave=$tipoDeChave, chave='$chave', tipoDeConta=$tipoDeConta, conta=$conta, id=$id, criadaEm=$criadaEm)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ChavePix) return false

        if (clienteId != other.clienteId) return false
        if (tipoDeChave != other.tipoDeChave) return false
        if (chave != other.chave) return false
        if (tipoDeConta != other.tipoDeConta) return false
        if (conta != other.conta) return false
        if (id != other.id) return false
        if (criadaEm != other.criadaEm) return false

        return true
    }

    override fun hashCode(): Int {
        var result = clienteId?.hashCode() ?: 0
        result = 31 * result + (tipoDeChave?.hashCode() ?: 0)
        result = 31 * result + (chave?.hashCode() ?: 0)
        result = 31 * result + (tipoDeConta?.hashCode() ?: 0)
        result = 31 * result + (conta?.hashCode() ?: 0)
        result = 31 * result + (id?.hashCode() ?: 0)
        result = 31 * result + criadaEm.hashCode()
        return result
    }


}

