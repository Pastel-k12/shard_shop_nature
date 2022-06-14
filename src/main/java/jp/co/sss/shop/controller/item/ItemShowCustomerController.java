package jp.co.sss.shop.controller.item;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Favorite;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.FavoriteRepository;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderItemRepository;

/**
 * 商品管理 一覧表示機能(一般会員用)のコントローラクラス
 *
 * @author SystemShared
 */
@Controller
public class ItemShowCustomerController {
	/**
	 * 商品情報
	 */
	@Autowired
	ItemRepository itemRepository;
	
	/**
	 * 商品情報
	 */
	@Autowired
	OrderItemRepository orderItemRepository;

	@Autowired
	FavoriteRepository favoriteRepository;
	
	/**
	 * トップ画面 表示処理
	 *
	 * @param model    Viewとの値受渡し
	 * @return "/" トップ画面へ
	 */
	@RequestMapping(path = "/")
	public String index(Model model) {
		List<Integer> itemIdSort = orderItemRepository.findIdSUMDescWithQuery();
		List<Integer> itemIdDelete = itemRepository.findIdWithQuery();
		List<Item> item = new ArrayList<>();
		for (Integer idSort : itemIdSort) {
			for (Integer idDelete : itemIdDelete) {
				if (idSort == idDelete) {
					item.add(itemRepository.getById(idSort));
					break;
				}
			}
		}
		model.addAttribute("items", item);
		return "index";
	}
	
	@RequestMapping(path = "/item/list/{sortType}")
	public String showItemList(@PathVariable int sortType, Model model, HttpSession session) {
		if(session.getAttribute("user") != null) {
			UserBean userBean = (UserBean) session.getAttribute("user");
			List<Favorite> favorites = favoriteRepository.findByUserIdOrderByItemId(userBean.getId());
			model.addAttribute("favorites", favorites);	
		}
		if (sortType == 1) {
			model.addAttribute("items", itemRepository.findByDeleteFlagOrderByInsertDateDescIdAsc(0));
		} else {
			List<Integer> itemIdSort = orderItemRepository.findIdSUMDescWithQuery();
			List<Integer> itemIdDelete = itemRepository.findIdWithQuery();
			List<Item> item = new ArrayList<>();
			for (Integer idSort : itemIdSort) {
				for (Integer idDelete : itemIdDelete) {
					if (idSort == idDelete) {
						item.add(itemRepository.getById(idSort));
						break;
					}
				}
			}
			model.addAttribute("items", item);
			model.addAttribute("sortType", "2");
		}

		return "item/list/item_list";
	}
	
	@RequestMapping(path = "/item/detail/{id}")
	public String showItem(@PathVariable int id, Model model) {
		model.addAttribute("item", itemRepository.getById(id));
		
		return "item/detail/item_detail";
	}

	@RequestMapping(path = "/item/list/category/{sortType}")
	public String showItemListCategory(@PathVariable int sortType, Integer categoryId, Model model, HttpSession session) {
		if(session.getAttribute("user") != null) {
			UserBean userBean = (UserBean) session.getAttribute("user");
			List<Favorite> favorites = favoriteRepository.findByUserIdOrderByItemId(userBean.getId());
			model.addAttribute("favorites", favorites);	
		}
		List<Integer> itemIdSort = new ArrayList<>();
		if (sortType == 1) {
			itemIdSort = itemRepository.findIdOrderByInsertDateDescWithQuery();
		} else {
			itemIdSort = orderItemRepository.findIdSUMDescWithQuery();
			model.addAttribute("sortType", "2");
		}
		List<Integer> itemIdCategory = itemRepository.findIdByCategoryWithQuery(categoryId);
		List<Item> item = new ArrayList<>();
		for (Integer idSort : itemIdSort) {
			for (Integer idCategory : itemIdCategory) {
				if (idSort == idCategory) {
					item.add(itemRepository.getById(idSort));
					break;
				}
			}
		}
		model.addAttribute("items", item);
		model.addAttribute("categoryId", categoryId);
		
		return "item/list/item_list";
	}

}
