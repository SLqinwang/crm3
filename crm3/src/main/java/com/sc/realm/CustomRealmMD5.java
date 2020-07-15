package com.sc.realm;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.sc.entity.XtPermissionInfo;
import com.sc.entity.XtUserAccount;
import com.sc.service.XtPermissionInfoService;
import com.sc.service.XtUserAccountService;

public class CustomRealmMD5 extends AuthorizingRealm {
	
	@Autowired
	XtUserAccountService xtUserAccountService;
	
	@Autowired
	XtPermissionInfoService XtPermissionInfoService;
	
	//�û���Ȩ
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		XtUserAccount account = (XtUserAccount) arg0.getPrimaryPrincipal();
		System.out.println("���ڸ���ǰ�û���Ȩ:");
		//�����ݿ��ѯ���û�ӵ����ЩȨ��
		List<String> list = new ArrayList<String>();
		List<XtPermissionInfo> perms = XtPermissionInfoService.getUserPerm(account.getUserId());
		if (perms!=null&&perms.size()>0) {
			System.out.println("���û�ӵ������Ȩ�ޣ�");
			for (XtPermissionInfo perm : perms) {
				String code = perm.getPermission();
				if(code!=null&&!code.equals("")) {
					System.out.println("========================="+code);
					list.add(code);//��Ȩ��Դ��ӵ�list����
				}
			}
		}
		SimpleAuthorizationInfo  info = new SimpleAuthorizationInfo();
		info.addStringPermissions(list);
		return info;

	}
	
	
	//�û���֤
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		
		String username = (String) token.getPrincipal();
		System.out.println("��ǰ����֤���û���:"+username);
		
		
		//1.��Ҫ�����ݿ��ѯ�Ƿ��и��û�
		XtUserAccount xtUserAccount = xtUserAccountService.login(username);
		if(xtUserAccount == null) {
			System.out.println("�����ڴ��û�");
			throw new UnknownAccountException(); 
		}
		//2.���û�������
		String password = xtUserAccount.getUserPassword();
		String salt = "qwerty";
		if ("��ͣ��".equals(xtUserAccount.getAccountState())) {
			throw new LockedAccountException();
		}
		//��һ��������������������object
		SimpleAuthenticationInfo info =new SimpleAuthenticationInfo(xtUserAccount, password,ByteSource.Util.bytes(salt), super.getName());
		return info;
	}

}
