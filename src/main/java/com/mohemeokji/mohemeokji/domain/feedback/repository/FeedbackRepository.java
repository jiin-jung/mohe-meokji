package com.mohemeokji.mohemeokji.domain.feedback.repository;

import com.mohemeokji.mohemeokji.domain.feedback.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}