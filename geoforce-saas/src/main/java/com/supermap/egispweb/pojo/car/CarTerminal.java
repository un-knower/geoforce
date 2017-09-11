package com.supermap.egispweb.pojo.car;

import com.supermap.egispservice.lbs.entity.Car;
import com.supermap.egispservice.lbs.entity.Terminal;

//import com.supermap.lbsp.provider.hibernate.lbsp.Car;
//import com.supermap.lbsp.provider.hibernate.lbsp.Terminal;

public class CarTerminal {
	private Car car;
	private Terminal terminal;
	public Car getCar() {
		return car;
	}
	public void setCar(Car car) {
		this.car = car;
	}
	public Terminal getTerminal() {
		return terminal;
	}
	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}

}
