package com.boredream.bdcodehelper.entity.city;

public class CityModel extends BaseAddressModel {
	public String province;
	public String letter;

	@Override
	public boolean equals(Object o) {
		if(o instanceof CityModel) {
			return ((CityModel)o).id.equals(this.id);
		}
		return super.equals(o);
	}
}
