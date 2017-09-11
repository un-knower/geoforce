package com.supermap.egispservice.base.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.supermap.egispservice.base.dao.InfoDeptDao;
import com.supermap.egispservice.base.entity.InfoDeptEntity;
import com.supermap.egispservice.base.entity.InfoDeptVO;
import com.supermap.egispservice.base.service.InfoDeptService;

/**
 * InfoDeptServiceImpl
 * 
 * @author QiuChao
 * 
 */
@Transactional
@Service
public class InfoDeptServiceImpl implements InfoDeptService {

	@Autowired
	private InfoDeptDao infoDeptDao;

	@Override
	public void saveDept(InfoDeptEntity dept) {
		InfoDeptEntity pDept = findDeptById(dept.getParentId());
		String code = genCode(pDept.getCode());
		dept.setCode(code);
		infoDeptDao.save(dept);
	}
	
	/* 
	public void updateDept(InfoDeptEntity dept) {
		// 当更改部门的上级部门的时候同时更改子部门下的所有code的值
		if (!findDeptById(dept.getId()).getParentId().equals(dept.getParentId())) {
			InfoDeptEntity pdept = findDeptById(dept.getParentId());
			String code = pdept.getCode();
			int length = (int) findAllDeptinfoLenghByCode(code);
			String code1 = pdept.getCode();
			int len = length;
			if (length == 0) {
				code = code + "0001";
			} else {
				len = length + 1;
				if (len < 10) {
					code = code + "000" + len;
				} else if (len < 100) {
					code = code + "00" + len;
				} else if (len < 1000) {
					code = code + "0" + len;
				} else if (len < 10000) {
					code = code + len;
				}
			}
			InfoDeptEntity di = getDeptByCode(code);
			while (di != null) {
				// deptId = updeptid + "001";
				// Long id = Long.parseLong(deptId) + length + 1;
				// deptId = Long.toString(id);
				len = len + 1;
				if (len < 10) {
					code = code1 + "000" + len;
				} else if (len < 100) {
					code = code1 + "00" + len;
				} else if (len < 1000) {
					code = code1 + "0" + len;
				} else if (len < 10000) {
					code = code1 + len;
				}
				di = getDeptByCode(code);
			}
			String dcode = dept.getCode();
			if (dcode != null) {
				int dcodeLen = dcode.length();
				List<InfoDeptEntity> list = getDeptLikeCode(dcode);
				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						InfoDeptEntity subDept = (InfoDeptEntity) list.get(i);
						String subCode = subDept.getCode();

						subCode = code + subCode.substring(dcodeLen);

						subDept.setCode(subCode);
						infoDeptDao.save(subDept);
					}
				}
			}
			dept.setCode(code);
		}
		
    	infoDeptDao.save(dept);
    }*/

	@Override
	public void updateDept(InfoDeptEntity dept) {
		// 由于不允许修改上级部门，所以暂不更新该部门及其子部门code
		infoDeptDao.save(dept);
	}

	@Override
	public void deleteDeptByIds(String ids) {
		infoDeptDao.delete(infoDeptDao.findAll(Arrays.asList(ids.split(","))));
	}

	@Override
	@Transactional(readOnly = true)
	public List<InfoDeptEntity> findAll() {
		return Lists.newArrayList(infoDeptDao.findAll());
	}

	@Override
	@Transactional(readOnly = true)
	public InfoDeptEntity findDeptById(String id) {
		return infoDeptDao.findById(id);
	}

	@Override
	public List<InfoDeptEntity> getChildDepts(String deptId) {
		return infoDeptDao.findByCodeLike(findDeptById(deptId).getCode() + "%");
	}

	@Override
	public Map<String, Object> getDepts(final String name, int pageNumber,
			int pageSize, String sortType, final String deptId) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType); 
		Specification<InfoDeptEntity> spec = new Specification<InfoDeptEntity>() {
			@Override
			public Predicate toPredicate(Root<InfoDeptEntity> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate codeLikePredicate = cb.like(root.get("code").as(String.class), 
						findDeptById(deptId).getCode() + "____%");
				
				Predicate nameLikePredicate = null;
				if (StringUtils.isNoneEmpty(name)) {
					nameLikePredicate = cb.like(root.get("name").as(String.class), 
							"%" + name.replaceAll("_", "/_").replaceAll("%", "/%") + "%", "/".charAt(0));
				}
				
				return nameLikePredicate == null ? codeLikePredicate : cb.and(nameLikePredicate, codeLikePredicate);
			}
		};
		Page<InfoDeptEntity> page = infoDeptDao.findAll(spec, pageRequest);
		List<InfoDeptEntity> entityList = page.getContent();
		
		List<InfoDeptVO> voList = new ArrayList<InfoDeptVO>(entityList.size());
		for(InfoDeptEntity entity : entityList) {
			InfoDeptVO vo = new InfoDeptVO();
			BeanUtils.copyProperties(entity, vo);
			InfoDeptEntity parent = findDeptById(entity.getParentId());
			if(parent != null) {
				vo.setParentName(parent.getName());
			}
			voList.add(vo);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", page.getTotalPages());
		map.put("page", pageNumber);
		map.put("records", page.getTotalElements());
		map.put("rows", voList);
		return map;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isReplicationNameInTheSameParent(String name, String parentId) {
		return infoDeptDao.findByNameAndParentId(name, parentId) == null ? false
				: true;
	}
	
	/**
	 * 部门code生成策略（需改进）
	 * @param pCode
	 * @return
	 */
	//TODO: 需要优化逻辑
	private String genCode(String pCode) {
		String code = pCode;
		String code1 = pCode;
		
		int length = (int) countDeptByCodeLike(code);
		int len = length;
		
		if (length == 0) {
			code = code + "0001";
		} else {
			len = length + 1;
			if (len < 10) {
				code = code + "000" + len;
			} else if (len < 100) {
				code = code + "00" + len;
			} else if (len < 1000) {
				code = code + "0" + len;
			} else if (len < 10000) {
				code = code + len;
			}
		}
		
		InfoDeptEntity dept = infoDeptDao.findByCode(code);
		while (dept != null) {
			len = len + 1;
			if (len < 10) {
				code = code1 + "000" + len;
			} else if (len < 100) {
				code = code1 + "00" + len;
			} else if (len < 1000) {
				code = code1 + "0" + len;
			} else if (len < 10000) {
				code = code1 + len;
			}
			dept = infoDeptDao.findByCode(code);
		}

		return code;
	}
	
	/**
	 * 统计部门中形如[code like '00001000____']的数量
	 * @param code
	 * @return
	 */
	private long countDeptByCodeLike(final String code) {
    	return infoDeptDao.count(new Specification<InfoDeptEntity>() {
			@Override
			public Predicate toPredicate(Root<InfoDeptEntity> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Path<String> codeExp = root.get("code");
				return cb.like(codeExp, code.trim() + "____");
			}
    	});
    }
	
	/**
	 * 构建PageRequest对象
	 * @param pageNumber
	 * @param pagzSize
	 * @param sortType
	 * @return
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("asc".equals(sortType)) {
			sort = new Sort(Direction.ASC, "id");
		} else if ("desc".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		}
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

	@Override
	public InfoDeptEntity findDeptByCode(String deptcode) {
		return this.infoDeptDao.findByCode(deptcode);
	}

}
