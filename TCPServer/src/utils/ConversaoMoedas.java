/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author isabela.barros
 */
public class ConversaoMoedas {

    private float dolar = 5.48f;
    private float euro = 6.34f;
    private float bitcoin = 53578.91f;

    public float dolarReal(float real) {
        return dolar*real;
        
    };

    
    public float euroReal(float real) {
        return euro*real;
    };
    
    public float bitcoinReal(float real) {
        return bitcoin*real;
    };

}
