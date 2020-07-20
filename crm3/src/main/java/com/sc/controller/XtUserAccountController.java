package com.sc.controller;

import java.util.Arrays;
import java.util.Date;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.sc.annotation.MyLog;
import com.sc.entity.Message;
import com.sc.entity.XtUserAccount;
import com.sc.service.XtUserAccountService;

/**
 * 
 * @author Sanji
 * @function ϵͳ�û��˻�
 */
@Controller
@RequestMapping("/xtaccountctrl")
public class XtUserAccountController {
	
	@Autowired
	XtUserAccountService xtUserAccountService;
	
	@MyLog("�鿴ϵͳ�û��˻�")
	@RequestMapping("/selectaccount.do")
	public ModelAndView selectAccount(ModelAndView mav,
			@RequestParam(defaultValue="1") Integer pageNum,
			@RequestParam(defaultValue="10") Integer pageSize,
			XtUserAccount xtUserAccount
			) {
		System.out.println("�����ҳ��ѯ�����ˣ�");
		PageInfo<XtUserAccount> page = xtUserAccountService.selectXtUserAccount(pageNum, pageSize, xtUserAccount);
		System.out.println("�����ǣ�"+page);
		mav.addObject("p",page);
		mav.addObject("xtUserAccount",xtUserAccount);
		mav.setViewName("xt/xtaccount_list");  // /WEB-INF/kc/cpdepot-list.jsp
		return mav;
	}
	
	/*
	 * @function ɾ������
	 */
	@MyLog("ɾ��ϵͳ�û��˻�")
	@RequestMapping("/deleteaccount.do")
	@ResponseBody
	public Message deleteAccount(XtUserAccount xtUserAccount) {
		System.out.println("���뵽��ɾ���˻�������");
		xtUserAccountService.deleteXtUserAccount(xtUserAccount.getUserId());
		return new Message("1", "success", "�ɹ�");
	}
	
	/*
	 * @function ��ת��ӽ��� or ��ת�޸Ľ���
	 */
	@MyLog(" ��ת��ӽ��� or ��ת�޸Ľ���")
	@RequestMapping("/goaccount.do")
	public ModelAndView goAddAccount(ModelAndView mav,XtUserAccount xtUserAccount) {
		if(xtUserAccount.getUserId()!=null) {
			xtUserAccount = xtUserAccountService.getXtUserAccount(xtUserAccount.getUserId());
		}
		mav.addObject("userAccount",xtUserAccount);
		mav.setViewName("xt/operator_account");
		return mav;
	}
	
	/*
	 * @function ����û� or �޸��û�
	 */
	@MyLog("����û��˻� or �޸��û��˻�")
	@RequestMapping("/operataccount.do")
	@ResponseBody
	public Message operatAccount(XtUserAccount xtUserAccount) {
		if(xtUserAccount.getUserId()!=null) {//�޸Ĳ���
			xtUserAccountService.updateXtUserAccount(xtUserAccount);	
		}else {//��Ӳ���
			SimpleHash s = new SimpleHash("md5", xtUserAccount.getUserPassword(), "qwerty", 3);
			xtUserAccount.setUserPassword(s.toString());
			xtUserAccountService.addXtUserAccount(xtUserAccount);
		}
		return new Message("1", "success", "�ɹ�");	
	}
	
	/*
	 * @function ��ת�޸��������
	 */
	@MyLog("��ת�޸��˻��������")
	@RequestMapping("/goupdatepass.do")
	public ModelAndView goUpdatePass(ModelAndView mav,XtUserAccount xtUserAccount) {
		XtUserAccount account = xtUserAccountService.getXtUserAccount(xtUserAccount.getUserId());
		mav.addObject("account",account);
		mav.setViewName("xt/change_password");
		return mav;
	}
	
	/*
	 * @function �޸����뷽��
	 */
	@MyLog("�޸��˻�����")
	@RequestMapping("/updatepass.do")
	@ResponseBody
	public Message updatePass(XtUserAccount xtUserAccount) {
		XtUserAccount account = xtUserAccountService.getXtUserAccount(xtUserAccount.getUserId());
		Date date = new Date();
		SimpleHash s = new SimpleHash("md5", xtUserAccount.getUserPassword(), "qwerty", 3);
		System.out.println("�޸ĺ������Ϊ:"+s.toString());
		account.setUserPassword(s.toString());
		//�޸����벢�޸Ĳ�����ʱ��
		account.setLastModifyDate(date);
		xtUserAccountService.updateXtUserAccount(account);
		return new Message("1", "success", "�ɹ�");
	}
	
	/*
	 * @function ͣ�� ����
	 */
	@MyLog("ͣ��/�����˻�")
	@RequestMapping("/updatestate.do")
	@ResponseBody
	public Message updateState(XtUserAccount xtUserAccount) {
		if(xtUserAccount.getUserId()!=null&&xtUserAccount.getAccountState()!=null) {
			Date date = new Date();
			System.out.println("���뵽�޸��û�״̬������"+xtUserAccount);
			XtUserAccount account = xtUserAccountService.getXtUserAccount(xtUserAccount.getUserId());
			//�޸�״̬�����޸�ʱ��
			account.setLastModifyDate(date);
			account.setAccountState(xtUserAccount.getAccountState());
			xtUserAccountService.updateXtUserAccount(account);
			return new Message("1", "success", "�ɹ�");
		}else {
			return new Message("2", "error", "����");
		}
	}
	
	/*
	 * @function ����ɾ��
	 */
	@MyLog("����ɾ���˻�")
	@RequestMapping("/deletemore.do")
	public ModelAndView deleteMore(ModelAndView mav,Long[] ids) {
		System.out.println("��������ɾ���û�������"+Arrays.toString(ids));
		if (ids!=null&&ids.length>0) {
			for (Long id : ids) {
				xtUserAccountService.deleteXtUserAccount(id);
			}
		}
		mav.setViewName("redirect:selectaccount.do");
		return mav;
	}
}
