package com.nanum.houseservice.room.infrastructure;

import com.nanum.houseservice.room.domain.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Page<Room> findAllByHouseId(Long houseId, Pageable pageable);
    boolean existsById(Long roomId);
    List<Room> findByHouseId(Long houseId);

    @Transactional
    @Modifying
    @Query(value = "update Room u set u.status = com.nanum.config.RoomStatus.PROGRESS where u.id = :roomId")
    void replaceStatusToProgress(Long roomId);

    @Transactional
    @Modifying
    @Query(value = "update Room u set u.status = com.nanum.config.RoomStatus.COMPLETION where u.id = :roomId")
    void replaceStatusToCOMPLETION(Long roomId);

    @Transactional
    @Modifying
    @Query(value = "update Room u set u.contractEndAt =:endDate where u.id = :roomId")
    void replaceDate(LocalDate endDate, Long roomId);
}
