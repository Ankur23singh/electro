package electro.repository;

import electro.model.ApiCallRecord;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface ApiCallRecordRepository extends JpaRepository<ApiCallRecord, Long> {
    boolean existsByBonusClaimTimeBetween(LocalDateTime start, LocalDateTime end);
//    Optional<BonusTransaction> findFirstByUserIdOrderByIdDesc(String userId);

    Optional<ApiCallRecord> findFirstByUserIdOrderByIdDesc(String userId);


    @Transactional
    List<ApiCallRecord> findByUserId(String userId);

    boolean existsByUserId(String userId);


}
