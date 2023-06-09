package com.exampm.spbbs.command;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.exampm.spbbs.dao.SpDao;
import com.exampm.spbbs.dto.SpDto;

public class SpRelpyCommand implements SpCommand {

	@Override
	public void execute(Model model) {

		Map<String, Object> map = model.asMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		String num = request.getParameter("num");

		SpDao dao = new SpDao();
		SpDto dto = dao.reply(num);
		
		model.addAttribute("reply", dto);
	}

}
