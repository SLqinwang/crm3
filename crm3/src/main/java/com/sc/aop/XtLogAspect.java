package com.sc.aop;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sc.annotation.MyLog;
import com.sc.entity.XtLog;
import com.sc.entity.XtUserAccount;
import com.sc.service.XtLogService;
import com.sc.service.XtUserAccountService;
import com.sc.utils.IpUtils;
/**
 * 
 * @author Sanji
 * @function ϵͳ��־�������ദ��
 */
@Aspect
@Component
public class XtLogAspect {
	
	@Autowired
	XtLogService xtLogService;
	
	@Autowired
	XtUserAccountService xtUserAccountService;
	
	//�����е� @Pointcut
    //��ע���λ���������
    @Pointcut("@annotation( com.sc.annotation.MyLog)")
    public void logPoinCut() {
    }
    
    //���� ����֪ͨ
    @AfterReturning("logPoinCut()")
    public void saveSysLog(JoinPoint joinPoint) {
        System.out.println("���档��������");
        //��ȡ��½�û���Ϣ
        XtUserAccount account = (XtUserAccount) SecurityUtils.getSubject().getPrincipal();
        XtUserAccount login = xtUserAccountService.login(account.getUserName());
        //System.out.println("��½�û�����Ϣ��:"+account);
        //������֯��㴦ͨ��������ƻ�ȡ֯��㴦�ķ���
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //������־
        XtLog xtLog = new XtLog();
        //��ȡ��������ڵķ���
        Method method = signature.getMethod();
        //��ȡ����
        MyLog myLog = method.getAnnotation(MyLog.class);
        if (myLog != null) {
            String value = myLog.value();
            xtLog.setPermission(value);//�����ȡ�Ĳ���
        }
        //����ʱ��
        xtLog.setVisitTime(new Date());
        xtLog.setUserId(login.getUserId());
        xtLog.setCompanyId(account.getCompanyId());
        //��ȡ�û�ip��ַ
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = IpUtils.getIpAddress(request);
        xtLog.setVisitIp(ip);
 
        //����service����XtLogʵ���ൽ���ݿ�
        xtLogService.addXtLog(xtLog);;
    }
}
