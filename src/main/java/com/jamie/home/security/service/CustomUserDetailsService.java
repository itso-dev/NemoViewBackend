package com.jamie.home.security.service;

import com.jamie.home.api.model.MEMBER;
import com.jamie.home.security.dao.SecurityDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

   private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

   @Autowired
   private SecurityDao securityDao;

   @Override
   @Transactional
   public UserDetails loadUserByUsername(final String email) {
      logger.debug("Try login.....::::" + email);
      MEMBER member = securityDao.getMemberForLogin(email);

      if(member == null){
         throw new UsernameNotFoundException(email+" ::: 데이터베이스에서 찾을 수 없습니다.");
      } else {
         List<GrantedAuthority> authorities = new ArrayList<>();
         authorities.add(new SimpleGrantedAuthority(member.getRole().toString()));

         return new User(member.getEmail(),member.getPassword(),authorities);
      }
   }

   public String getCurrentId() {
      try {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         User user = (User) authentication.getPrincipal();
         return user.getUsername();
      } catch (Exception e){
         return null;
      }
   }
}
