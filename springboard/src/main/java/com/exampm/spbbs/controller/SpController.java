package com.exampm.spbbs.controller;

import javax.servlet.http.HttpServletRequest;
import javax.swing.AbstractListModel;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.exampm.spbbs.command.SpCommand;
import com.exampm.spbbs.command.SpDeleteCommand;
import com.exampm.spbbs.command.SpDetailCommand;
import com.exampm.spbbs.command.SpListCommand;
import com.exampm.spbbs.command.SpRelpyCommand;
import com.exampm.spbbs.command.SpRelpyokCommand;
import com.exampm.spbbs.command.SpUpdateCommand;
import com.exampm.spbbs.command.SpUpdateokCommand;
import com.exampm.spbbs.command.SpWriteCommand;

@Controller
public class SpController {

	// 모든 commnad가 갖고 있는 인터페이스 타입을 선언
	SpCommand command;

	@RequestMapping("/list")
	public String list(Model model) {
		System.out.println("list() 실행");

		command = new SpListCommand();

		command.execute(model);

		return "list";
	}

	@RequestMapping("/detail")
	public String detail(HttpServletRequest request, Model model) {
		System.out.println("detail()");
		model.addAttribute("request", request);
		command = new SpDetailCommand();
		command.execute(model);
		return "detail";
	}

	@RequestMapping("/write")
	public String write(Model model) {
		System.out.println("write()");
		return "write";
	}

	@RequestMapping(value = "/writeok", method = RequestMethod.POST)
	public String writeok(HttpServletRequest request, Model model) {
		System.out.println("writeok");

		model.addAttribute("request", request);
		command = new SpWriteCommand();
		command.execute(model);
		return "redirect:list";
	}

	@RequestMapping("/reply")
	public String reply(HttpServletRequest request, Model model) {
		System.out.println("reply()");

		model.addAttribute("request", request);
		command = new SpRelpyCommand();
		command.execute(model);

		return "reply";
	}

	@RequestMapping(value = "/replyok", method = RequestMethod.POST)
	public String replyok(HttpServletRequest request, Model model) {
		System.out.println("replyok()");

		model.addAttribute("request", request);
		command = new SpRelpyokCommand();
		command.execute(model);

		return "redirect:list";
	}

	@RequestMapping("/update")
	   public String update(HttpServletRequest request, Model model) {
			System.out.println("update()");
	      
	      model.addAttribute("request", request);
	      command = new SpUpdateCommand();
	      command.execute(model);
	      
	      return "update";
	   }

		@RequestMapping(value = "/updateok", method = RequestMethod.POST)
		public String updateok(HttpServletRequest request, Model model) {
			System.out.println("updateok()");

			model.addAttribute("request", request);
			command = new SpUpdateokCommand();
			command.execute(model);

			return "update";
		}

		@RequestMapping("/delete")
		public String delete(HttpServletRequest request, Model model) {
			System.out.println("delete()");

			model.addAttribute("request", request);
			command = new SpDeleteCommand();
			command.execute(model);

			return "redirect:list";
		}

}
