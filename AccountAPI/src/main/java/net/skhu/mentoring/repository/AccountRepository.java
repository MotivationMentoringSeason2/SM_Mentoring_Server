package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.Account;
import net.skhu.mentoring.enumeration.UserType;
import net.skhu.mentoring.model.AccountPagination;
import net.skhu.mentoring.model.OptionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends AccountBaseRepository<Account> {
    List<Integer> sizeBy = Arrays.asList(4, 6, 8, 10, 12, 14, 16, 18, 20);
    List<OptionModel> searchBy = Arrays.asList(new OptionModel(1, "회원 아이디 포함 검색"), new OptionModel(2, "회원 이름 포함 검색"), new OptionModel(3, "회원 주 학과 포함 검색"));
    List<OptionModel> orderBy = Arrays.asList(new OptionModel(1, "회원 가입 순"), new OptionModel(2, "회원 가입 역순"), new OptionModel(3, "회원 이름 순"), new OptionModel(4, "회원 이름 역순"), new OptionModel(5, "회원 아이디 순"), new OptionModel(6, "회원 아이디 역순"));

    Sort[] sort = {
            new Sort(Sort.Direction.ASC, "id"),
            new Sort(Sort.Direction.DESC, "id"),
            new Sort(Sort.Direction.ASC, "name"),
            new Sort(Sort.Direction.DESC, "name"),
            new Sort(Sort.Direction.ASC, "identity"),
            new Sort(Sort.Direction.DESC, "identity")
    };

   default List<Account> findAll(AccountPagination accountPagination){
        Pageable pageable = new PageRequest(accountPagination.getPg() - 1, accountPagination.getSz(), sort[(accountPagination.getOb() != 0) ? accountPagination.getOb() - 1 : accountPagination.getOb()]);
        Page<Account> page;
        String type = null;

        switch(accountPagination.getTb()){
            case 1 :
                type = UserType.STUDENT;
                break;
            case 2 :
                type = UserType.PROFESSOR;
                break;
            case 3 :
                type = UserType.EMPLOYEE;
                break;
        }

        String searchText = accountPagination.getSt();
        switch(accountPagination.getSb()){
            case 1 :
                page = (type == null) ? this.findByIdentityContains(searchText, pageable) : this.findByTypeAndIdentityContains(type, searchText, pageable);
                break;
            case 2 :
                page = (type == null) ? this.findByNameContains(searchText, pageable) : this.findByTypeAndNameContains(type, searchText, pageable);
                break;
            case 3 :
                page = (type == null) ? this.findByDepartmentNameContains(searchText, pageable) : this.findByTypeAndDepartmentNameContains(type, searchText, pageable);
                break;
            default :
                page = (type == null) ? this.findAll(pageable) : this.findByType(type, pageable);
                break;
        }
        accountPagination.setRequestCount(page.getTotalElements());
        return page.getContent();
    }

    Page<Account> findAll(Pageable pageable);
    Page<Account> findByIdentityContains(String identity, Pageable pageable);
    Page<Account> findByNameContains(String name, Pageable pageable);
    Page<Account> findByDepartmentNameContains(String departmentName, Pageable pageable);

    Page<Account> findByType(String type, Pageable pageable);
    Page<Account> findByTypeAndIdentityContains(String type, String identity, Pageable pageable);
    Page<Account> findByTypeAndNameContains(String type, String name, Pageable pageable);
    Page<Account> findByTypeAndDepartmentNameContains(String type, String departmentName, Pageable pageable);

    Optional<Account> findByNameAndEmail(String name, String email);
    Optional<Account> findByIdentity(String identity);
    Optional<Account> findByIdentityAndEmail(String name, String email);
    boolean existsByIdentity(String identity);
}
