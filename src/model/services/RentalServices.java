package model.services;

import model.entities.CarRental;
import model.entities.Invoice;

/**
 * Inversão de controle: Padrão de desenvolvimento que consiste em retirar da classe a responsabilidade de instanciar 
 * suas dependências.
 * Injecao de dependencia: E uma forma de realizar inversao de controle: um componente externo (Classe Programa) instancia
 * a dependencia, que e entao injetada no objeto "pai" (Classe RentalService). Pode ser implementada de várias formas:
 * Construtor
 * Clase de instanciacao (builder/factory)
 * Container/Framework
 * 
 *
 */
public class RentalServices {
	
	private Double pricePerDay;
	private Double pricePerHour;
	
	/**
	 * Dependencia com a Interface e nao com a classe concreta (BrazilTaxService).
	 */
	private TaxService taxService;

	public RentalServices(Double pricePerDay, Double pricePerHour, TaxService taxService) {
		super();
		this.pricePerDay = pricePerDay;
		this.pricePerHour = pricePerHour;
		this.taxService = taxService;
	}
	
	public void processInvoice(CarRental carRental) {
		long t1 = carRental.getStart().getTime();
		long t2 = carRental.getFinish().getTime();
		
		double hours = convertInHours(t1, t2);
		
		double basicPayment;
		if (hours <= 12.0) {
			basicPayment = Math.ceil(hours) * pricePerHour;
		}
		else {
			basicPayment = Math.ceil(hours/24) * pricePerDay;
		}
		
		double tax = taxService.tax(basicPayment);
		
		carRental.setInvoice(new Invoice(basicPayment, tax));
	}

	private double convertInHours(long t1, long t2) {
		return (double) (t2 -t1) /1000/ 60/ 60;
	}

}
