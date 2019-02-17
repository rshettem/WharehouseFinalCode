package com.app.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.app.dao.IItemDao;
import com.app.model.Item;

@Repository
public class ItemDaoImpl implements IItemDao {

	@Autowired
	private HibernateTemplate ht;

	public Integer saveItem(Item item) {
		return (Integer) ht.save(item);
	}

	public void updateItem(Item item) {
		ht.update(item);

	}

	public void deleteItem(Integer itemId) {

		ht.delete(new Item(itemId));

	}

	public Item getItemById(Integer itemId) {
		return ht.get(Item.class, itemId);
	}

	public List<Item> getAllItems() {
		return ht.loadAll(Item.class);
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public boolean isItemCodeExist(String itemCode) {

		long count=0;
		String hql= "select count(itemCode) from "+Item.class.getName()+" where itemCode=?";
		List<Long> items=(List<Long>) ht.find(hql, itemCode);
		if (items!=null && !items.isEmpty()) {
			count=items.get(0);
		}
		return count>0?true:false;
	}
}
