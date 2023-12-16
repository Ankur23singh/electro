package electro.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "Portfolio")
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;


    @Column(name = "total_assets")
    private BigDecimal totalAssets;

    @Column(name = "team_income")
    private BigDecimal teamIncome;

    @Column(name = "todays_income")
    private BigDecimal todaysIncome;

    @Column(name = "total_income")
    private BigDecimal totalIncome;

    @Column(name = "current_balance")
    private BigDecimal currentBalance;

    @Column(name="referral_status", length = 1000)
    private String referralStatus;

    @Column(name = "bonus")
    private int bonus;
}
