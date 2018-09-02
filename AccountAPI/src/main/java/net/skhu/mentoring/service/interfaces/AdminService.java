package net.skhu.mentoring.service.interfaces;

import net.skhu.mentoring.vo.UserViewVO;

import java.util.List;

public interface AdminService {
    List<UserViewVO> fetchAllBriefUsers();
}
