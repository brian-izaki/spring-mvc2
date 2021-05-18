package com.projetojava.brewer.validation.validator;

import com.projetojava.brewer.validation.AtributoConfirmacao;
import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

// o uso da Classe Object é para deixar genérico, podendo ser usado para outras classes.
public class AtributoConfirmacaoValidator implements ConstraintValidator<AtributoConfirmacao, Object> {

    private String atributo;
    private String atributoConfirmacao;

    @Override
    public void initialize(AtributoConfirmacao constraintAnnotation) {
        // está sendo passado os campos da model especificadas no Bean.
        this.atributo = constraintAnnotation.atributo();
        this.atributoConfirmacao = constraintAnnotation.atributoConfirmacao();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        boolean valido;

        try {
            // Bean utils está sendo usado por facilitar a manipulação do objeto.
            // nesse exemplo ele pega um objeto genérico e atribui o valor para essa objeto.
            // (podendo ser String, int, etc)
            Object valorAtributo = BeanUtils.getProperty(object, this.atributo);
            Object valorAtributoConfirmacao = BeanUtils.getProperty(object, this.atributoConfirmacao);

            valido = ambosSaoNull(valorAtributo, valorAtributoConfirmacao)
                    || ambosSaoIguais(valorAtributo, valorAtributoConfirmacao);
        } catch (Exception e) {
            throw new RuntimeException("Erro recuperando valores dos atributos", e);
        }

        if (!valido) {
            context.disableDefaultConstraintViolation(); // desabilita a mensagem default, para que não haja duas mensagens iguais
            String mensagem = context.getDefaultConstraintMessageTemplate(); // pega o atributo message passado no bean
            // irá construir uma nova mensagem
            ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder = context
                    .buildConstraintViolationWithTemplate(mensagem);
            // add a validação de erro para o campo que for atributoConfirmação
            violationBuilder.addPropertyNode(atributoConfirmacao).addConstraintViolation();
        }

        return valido;
    }

    private boolean ambosSaoIguais(Object valorAtributo, Object valorAtributoConfirmacao) {
        return valorAtributo != null && valorAtributo.equals(valorAtributoConfirmacao);
    }

    private boolean ambosSaoNull(Object valorAtributo, Object valorAtributoConfirmacao) {
        return valorAtributo == null && valorAtributoConfirmacao == null;
    }
}
