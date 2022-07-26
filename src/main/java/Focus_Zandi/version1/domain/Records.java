package Focus_Zandi.version1.domain;

import Focus_Zandi.version1.domain.dto.RecordsDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter @Setter
@Entity
@Table(name = "Records")
public class Records {

    @ManyToOne(fetch = FetchType.LAZY) // 연관관계가 이게 맞나
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Id @GeneratedValue
    @Column(name = "RECORD_ID")
    private long recordId;

    @Column(nullable = false)
    private int brokenCounter;

    @Column(nullable = false)
    private int maxConcentrationTime;

    @Column(nullable = false)
    private int total_time;

    @Column(nullable = false)
    private String year;

    @Column(nullable = false)
    private String month;

    @Column(nullable = false)
    private String day;

    // 비즈니스 로직 //
    public static Records createRecords(Member member, RecordsDto recordsDto) {
        Records records = new Records();
        records.setMember(member);
        List<Integer> dataHolder = recordsDto.getConcentratedTime();
        records.setYear(recordsDto.getTimeStamp().substring(0,4));
        records.setMonth(recordsDto.getTimeStamp().substring(5,7));
        records.setDay(recordsDto.getTimeStamp().substring(8,10));

        int max = 0;
        int sum = 0;
        for (Integer integer : dataHolder) {
            sum += integer;
            if (integer > max) max = integer;
        }
        records.setBrokenCounter(dataHolder.size());
        records.setMaxConcentrationTime(max);
        records.setTotal_time(sum);
        return records;
    }
}
