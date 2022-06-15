package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jp.co.sss.shop.entity.OrderItem;

/**
 * order_itemsテーブル用リポジトリ
 *
 * @author System Shared
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
	
	/** OrderItemテーブルから商品IDを売れた個数の降順で取り出す*/
	@Query("SELECT oi.item.id FROM OrderItem oi GROUP BY oi.item.id ORDER BY SUM(oi.quantity) DESC, oi.item.id ASC")
	public List<Integer> findIdSUMDescWithQuery();
	
	
	//購入された商品の商品Idを検索
	@Query("select oi.item.id from OrderItem oi")
	public List<Integer> findIdWithQuery();

}
