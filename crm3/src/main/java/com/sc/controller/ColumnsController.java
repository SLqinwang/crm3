package com.sc.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.sc.annotation.MyLog;
import com.sc.entity.Message;
import com.sc.entity.XtPermissionColumns;
import com.sc.entity.XtPermissionInfo;
import com.sc.service.XtPermissionColumnsService;
import com.sc.service.XtPermissionInfoService;

@Controller
@RequestMapping("/permctrl")
public class ColumnsController {
	
	@Autowired
	XtPermissionColumnsService xtPermissionColumnsService;
	
	@Autowired
	XtPermissionInfoService xtPermissionInfoService;
	
	@MyLog("��ѯȨ�޷�����Ϣ")
	@RequestMapping("/selectcolumns.do")
	public ModelAndView selectXtLog(ModelAndView mav,
			@RequestParam(defaultValue="1") Integer pageNum,
			@RequestParam(defaultValue="10") Integer pageSize,
			XtPermissionColumns xpc) {
		PageInfo<XtPermissionColumns> page = xtPermissionColumnsService.selectPermColumns(pageNum, pageSize, xpc);
		mav.addObject("p",page);
		mav.setViewName("xt/xtpermcolumns_list"); 
		return mav;
	}
	
	@MyLog("��ת���Ȩ�޷�����Ϣ")
	@RequestMapping("/goaddpermcolumns.do")
	public ModelAndView goAddColumns(ModelAndView mav) {
		mav.setViewName("xt/add_columns");
		return mav;
	}
	
	@MyLog("���Ȩ�޷�����Ϣ")
	@RequestMapping("/addpermcolumns.do")
	@ResponseBody
	public Message addPermColumns(ModelAndView mav,XtPermissionColumns xpc) {
		System.out.println("�������Ȩ�޷�����Ϣ"+xpc);
		xtPermissionColumnsService.addColumns(xpc);
		return new Message("1", "success", "�ɹ�");
	}
	
	@MyLog("�鿴��ǰ�����µ�Ȩ��")
	@RequestMapping("/findin.do")
	public ModelAndView findAtThisPerm(ModelAndView mav,
			Long columnsId,
			@RequestParam(defaultValue="1") Integer pageNum,
			@RequestParam(defaultValue="10") Integer pageSize) {

		PageInfo<XtPermissionInfo> page = xtPermissionInfoService.selectByColumnId(pageNum, pageSize, columnsId);
		mav.addObject("p",page);
		mav.addObject("columnsId",columnsId);
		mav.setViewName("xt/find_permissioninfo");
		return mav;
	}
	
	@MyLog("�鿴���ڵ�ǰ�����µ�Ȩ��")
	@RequestMapping("/findnotin.do")
	public ModelAndView findNotAtThisPerm(ModelAndView mav,
			Long columnsId,
			@RequestParam(defaultValue="1") Integer pageNum,
			@RequestParam(defaultValue="10") Integer pageSize) {
		PageInfo<XtPermissionInfo> page = xtPermissionInfoService.selectNotByColumnId(pageNum, pageSize, columnsId);
		mav.addObject("p",page);
		mav.addObject("columnsId",columnsId);
		mav.setViewName("xt/permissioninfo_add");
		return mav;
	}
	
	@MyLog("���Ȩ�޵�����")
	@RequestMapping("/addpermtocolumns.do")
	public ModelAndView addPermToColumns(ModelAndView mav,Long[] ids,BigDecimal columnsId) {
		if (ids!=null&&ids.length>0) {
			for (Long id : ids) {
				XtPermissionInfo info = xtPermissionInfoService.getPermInfo(id);
				info.setPermissionColumnsId(columnsId);
				xtPermissionInfoService.updateInfo(info);
			}
		}
		mav.setViewName("redirect:findnotin.do?columnsId="+columnsId);
		return mav;
	}
	
	@MyLog("ɾ��������Ϣ")
	@RequestMapping("/deletecolumn.do")
	@ResponseBody
	public Message deleteColumn(ModelAndView mav,Long columnsId) {
		xtPermissionColumnsService.deleteColumns(columnsId);
		return new Message("1", "success", "�ɹ�");
	}
	
	@MyLog("����ɾ��������Ϣ")
	@RequestMapping("/deletecolumns.do")
	public ModelAndView deleteColumns(ModelAndView mav,Long[] ids) {
		if (ids!=null&&ids.length>0) {
			for (Long id : ids) {
				xtPermissionColumnsService.deleteColumns(id);
			}
		}
		mav.setViewName("redirect:selectcolumns.do");
		return mav;
	}
	
	@MyLog("��ת�޸ķ�����Ϣ")
	@RequestMapping("/goupdatecolumn.do")
	public ModelAndView goUpdateColumn(ModelAndView mav,Long columnsId) {
		XtPermissionColumns columns = xtPermissionColumnsService.getColumns(columnsId);
		mav.addObject("columns",columns);
		mav.setViewName("xt/update_columns");
		return mav;
	}
	
	@MyLog("�޸ķ�����Ϣ")
	@RequestMapping("/updatecolumn.do")
	@ResponseBody
	public Message updateColumn(ModelAndView mav,XtPermissionColumns columns) {
		xtPermissionColumnsService.updateColumns(columns);
		mav.addObject("columns",columns);
		return new Message("1", "success", "�ɹ�");	
	}
	
}
