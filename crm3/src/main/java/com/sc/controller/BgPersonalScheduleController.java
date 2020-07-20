package com.sc.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.sc.entity.BgPersonalSchedule;
import com.sc.entity.Message;
import com.sc.service.BgPersonalScheduleService;

@Controller
@RequestMapping("/bgpersonalschedule")
public class BgPersonalScheduleController {

	@Autowired
	BgPersonalScheduleService bgPersonalScheduleService;
	
	@RequestMapping("/selectschedule.do")
	public ModelAndView selectSchedule(ModelAndView mav,
			@RequestParam(defaultValue="1") Integer pageNum,
			@RequestParam(defaultValue="5") Integer pageSize,
			BgPersonalSchedule schedule){
		PageInfo<BgPersonalSchedule> page=bgPersonalScheduleService.selectSchedule(pageNum, pageSize, schedule);
		
		mav.addObject("p",page);
		mav.addObject("s",schedule);
		mav.setViewName("bg/bgschedule-list");
		return mav;
	}
	
	@RequestMapping("/goaddschedule.do")
	public ModelAndView goAddschedule(ModelAndView mav,BgPersonalSchedule schedule){
		System.out.println("进入添加页面"+schedule);
		
		if(schedule.getSchedulePlanId()!=null){
			schedule=bgPersonalScheduleService.getSchedule(schedule.getSchedulePlanId());
		}
		mav.setViewName("bg/bgschedule-add");
		mav.addObject("schedule", schedule);
		return mav;
	}
	
	@RequestMapping("/addschedule.do")
	@ResponseBody
	public void addSchedule(ModelAndView mav,BgPersonalSchedule schedule){
		System.out.println("进入添加方法："+schedule);
		if(schedule.getSchedulePlanId()!=null){
			bgPersonalScheduleService.updateSchedule(schedule);
		}else{
			bgPersonalScheduleService.addSchedule(schedule);
		}
	}
	
	@RequestMapping("/deleteschedule.do")
	@ResponseBody
	public Message deleteSchedule(ModelAndView mav,BgPersonalSchedule schedule){
		bgPersonalScheduleService.deleteSchedule(schedule.getSchedulePlanId());
		return new Message("1", "success", "成功");
	}
	
	@RequestMapping("/deletescheduleall.do")
	public String deleteScheduleAll(ModelAndView mav,Long[] ids){
			
		System.out.println("进入批量删除:"+Arrays.toString(ids));
		if(ids!=null&&ids.length>0){
			for (Long id : ids) {
				bgPersonalScheduleService.deleteSchedule(id);
			}
		}
		return "redirect:selectschedule.do";
    }
}