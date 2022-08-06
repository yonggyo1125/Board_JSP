package dbtest;

import java.util.*;
import models.member.MemberDao;

import models.member.*;

public class MemberTest {
	public static void main(String[] args) {
		MemberDao dao = MemberDao.getInstance();
		List<MemberDto> members = dao.gets();
		System.out.println(members);
	}
}
