package com.base.basesetup.entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.base.basesetup.dto.CreatedUpdatedDate;
import com.base.basesetup.dto.Gender;
import com.base.basesetup.dto.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usersgen")
	@SequenceGenerator(name = "usersgen", sequenceName = "usersseq", initialValue = 1000000001, allocationSize = 1)
	
	@Column(name = "userid")
	private Long userId;
//	private String firstName;
//	private String lastName;
	@Column(name = "email")
	private String email;
	@Column(name = "username")
	private String userName;
	@Column(name = "password")
	private String password;
	
	@Column(name = "employeename")
	private String employeeName;
	@Column(name = "nickname")
	private String nickName;
	@Column(name = "createdby")
	private String createdby;
	@Column(name = "modifiedby")
	private String updatedby;
//	private String phone;
//	private String secondaryPhone;
	@Column(name = "loginstatus")
	private boolean loginStatus;
	@Column(name = "isActive")
	private boolean isActive;
//	@Enumerated(EnumType.STRING)
//	private Gender gender;
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-dd-MM")
//	private LocalDate dob;
	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private Role role;

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

	private Date accountRemovedDate;
}
