package com.app.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.app.dao.IShipmentTypeDao;
import com.app.model.ShipmentType;
import com.app.util.AppCollectionUtil;

@Repository
public class ShipmentTypeDaoImpl implements IShipmentTypeDao {
	
	@Autowired
	private HibernateTemplate ht;

	public Integer saveShipmentType(ShipmentType shipmentType) {
		
		return (Integer) ht.save(shipmentType);
		 
	}

	public void updateShipmentType(ShipmentType shipmentType) {
		
		ht.update(shipmentType);

	}

	public void deleteShipmentType(Integer shipmentId) {
		
		ht.delete(new ShipmentType(shipmentId));

	}

	public ShipmentType getShipmentTypeById(Integer shipmentId) {
		return ht.get(ShipmentType.class, shipmentId);
	}

	public List<ShipmentType> getAllShipmentTypes() {
		return ht.loadAll(ShipmentType.class);
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<Object[]> getShipmentTypeCount() {

		String hql =   " select shipmentMode,count(shipmentMode) from "
				     + ShipmentType.class.getName()
				     + " group by shipmentMode";
		List<Object[]> shipmentTypes=(List<Object[]>) ht.find(hql);
		
		return shipmentTypes;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public boolean isShipmentCodeExist(String shipmentCode) {
		
		long count=0;
		String hql="select count(shipmentCode) from "+ShipmentType.class.getName()+" where shipmentCode=?";
		List<Long> shipmentTypes=(List<Long>) ht.find(hql, shipmentCode);
		if (shipmentTypes!=null && !shipmentTypes.isEmpty()) {
			count=shipmentTypes.get(0);
		}
		return count>0?true:false;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public Map<Integer, String> getEnableShipmentIdsAndCodes() {
		
		String hql = "select shipmentid,shipmentCode from "+ShipmentType.class.getName()+" where enableShipment=?";
		return AppCollectionUtil.toMap((List<Object[]>) ht.find(hql, "YES"));
	}

}
