package com.maticz.Karting.results.API.repository;

import com.maticz.Karting.results.API.model.RunResults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RunResultsRepository extends JpaRepository<RunResults, Integer> {

    Optional<RunResults> findByIdContactAndScheduledAt(Integer idContact, LocalDateTime scheduledAt);

    @Query(value =
                    " select a.* " +
                            ", case when a.kvalificiral = 1 and a.idSpeedCategory = a.maxCategory then 1 else 0 end niVozil1  " +
                            " from ( " +
                            "select a.idcontact, " +
                            "category_name, " +
                            "a.best_lap clientAllTimeBest " +
                            ",d.scheduled_at dateOfBest " +
                            ", lastVisit " +
                            ",e.best_lap lastVisitLapTime " +
                            ",e.best_lap - a.best_lap difBestVSLast " +
                            ",a.idSpeedCategory " +
                            ",qualify_time " +
                            ", case when b.qualify_time > a.best_lap then 1 else 0 end kvalificiral " +
                            ", case when a.best_lap-b.qualify_time<= 0 then 0 else a.best_lap-b.qualify_time end manjka_do_kvalif " +
                            ", DENSE_RANK() over (partition by a.idSpeedCategory order by a.best_lap) mesto_v_hitrostnem_Razredu " +
                            ", min(a.best_lap) over (partition by a.idSpeedCategory order by a.best_lap) najhitrejsi_voznik " +
                            ", a.best_lap - min(a.best_lap) over (partition by a.idSpeedCategory order by a.best_lap) do_prvega_mesta " +
                            ", f.email " +
                            ",case when f.idContact = f.idContactAdmin then 1 else 0 end parent1child0 " +
                            ",g.maxCategory " +
                            "from " +
                            "(select idcontact, idSpeedCategory , min(best_lap)best_lap, max(scheduled_at)lastVisit from RF_Fact_Run_Results  " +
                            "group by idcontact, idSpeedCategory) a " +
                            "left join (select * from RF_Ref_Qualify_Times where active =1 and idtrack =1 ) b on a.idSpeedCategory = b.idSpeedCategory " +
                            "left join (select distinct idSpeedCategory ,category_name from RF_Ref_Speed) c on a.idSpeedCategory = c.idSpeedCategory " +
                            "left join RF_Fact_Run_Results d on d.best_lap = a.best_lap and a.idcontact = d.idcontact " +
                            "left join RF_Fact_Run_Results e on e.scheduled_at = a.lastVisit and e.idcontact = a.IdContact and a.idSpeedCategory = e.idSpeedCategory " +
                            "left join contact f on f.idContact = a.idcontact " +
                            "left join (select max(idSpeedCategory) maxCategory, idcontact  from RF_Fact_Run_Results group by idcontact) g on g.idcontact = a.idcontact " +
                            "where a.idSpeedCategory> 300052 " +
                            "and month(cast(d.scheduled_at  as date)) = :month " +
                            ") a " +
                            "where parent1child0 = 1 ", nativeQuery = true)
    List<Object[]> test(@Param("month") Integer month);


    @Query(value = "select a.* " +
            ", case when a.kvalificiral = 1 and a.idSpeedCategory = a.maxCategory then 1 else 0 end niVozil1 " +
            ", pozicija - starapozicija padel_za_št_mest " +
            " from ( " +
            "select a.idcontact, " +
            "category_name, " +
            "a.best_lap clientAllTimeBest " +
            ",d.bestDate " +
            ", lastVisit " +
            ",e.best_lap lastVisitLapTime " +
            ",e.best_lap - a.best_lap difBestVSLast " +
            ",a.idSpeedCategory " +
            ",qualify_time " +
            ", case when b.qualify_time > a.best_lap then 1 else 0 end kvalificiral " +
            ", case when a.best_lap-b.qualify_time<= 0 then 0 else a.best_lap-b.qualify_time end manjka_do_kvalif " +
            ", DENSE_RANK() over (partition by a.idSpeedCategory, a.idTrack  order by a.best_lap) pozicija " +
            ", min(a.best_lap) over (partition by a.idSpeedCategory, a.idtrack order by a.best_lap) najhitrejsi_voznik " +
            ", a.best_lap - min(a.best_lap) over (partition by a.idSpeedCategory order by a.best_lap) do_prvega_mesta " +
            ", k.starapozicija " +
            ", f.email " +
            ",case when f.idContact = f.idContactAdmin then 1 else 0 end parent1child0 " +
            ",case when g.maxCategory = 300053 then 'Dirkač' " +
            " when g.maxCategory = 300054 then 'Pro' " +
            " when g.maxCategory = 300059 then 'Legenda' " +
            " else 'drugo' end maxCategoryName " +
            ",g.maxCategory " +
            ",a.idtrack " +
            "from " +
            "(select idcontact, idSpeedCategory , min(best_lap)best_lap, max(scheduled_at)lastVisit, idtrack from RF_Fact_Run_Results " +
            "group by idcontact, idSpeedCategory, idtrack) a " +
            "left join (select * from RF_Ref_Qualify_Times where   idtrack =1 ) b on a.idSpeedCategory = b.idSpeedCategory " +
            "left join (select distinct idSpeedCategory ,category_name from RF_Ref_Speed) c on a.idSpeedCategory = c.idSpeedCategory " +
            "left join (select max(scheduled_at) bestDate, idcontact, best_lap  from RF_Fact_Run_Results group by idcontact, best_lap ) d on d.best_lap = a.best_lap and a.idcontact = d.idcontact " +
            "left join RF_Fact_Run_Results e on e.scheduled_at = a.lastVisit and e.idcontact = a.IdContact and a.idSpeedCategory = e.idSpeedCategory " +
            "left join contact f on f.idContact = a.idcontact " +
            "left join (select max(idSpeedCategory) maxCategory, idcontact  from RF_Fact_Run_Results group by idcontact) g on g.idcontact = a.idcontact " +
            "left join (select dense_rank() over (partition by k.idspeedcategory order by k.best_lap) starapozicija, k.* " +
            "from  " +
            "(select idcontact, idSpeedCategory, min(best_lap)best_lap from RF_Fact_Run_Results where month(scheduled_at) < month(dateadd(month,-1,getdate())) and year(scheduled_at) = year(GETDATE()) group by idcontact, idSpeedCategory)k " +
            ") k on k.idcontact = a.idcontact and k.idspeedcategory = a.idspeedcategory and k.best_lap = a.best_lap  " +
            "where a.idSpeedCategory> 300052 " +
            ") a " +
            "where parent1child0 = 1 " , nativeQuery = true)
            List<Object[]> kartingQuery();


    @Query(value = " select  a.visit_date,a.idtrack,a.idcontact,a.idspeedcategory,a.category_name,a.kvalificiral,a.Zaostastanek_za_kvalifikacijo ,a.za_prvim_zaostajas,a.contactemail,a.best_lap " +
            ", b.rank_that_day, c.rank_today " +
            "from " +
            "(select visit_date " +
            ", a.idTrack " +
            ", a.idcontact " +
            ", idSpeedCategory " +
            ", category_name " +
            ", kvalificiral " +
            ", case when kvalificiral <> 1 " +
            "       then best_lap-qualify_time  " +
            "       else null end Zaostastanek_za_kvalifikacijo " +
            " " +
            ", best_lap-najhitrejsi_voznik za_prvim_zaostajas " +
            ", contactemail, best_lap " +
            "from (select idcontact, category_name,a.idSpeedCategory,best_lap,qualify_time,visit_date,a.idTrack " +
            "            , case when b.qualify_time > best_lap then 1 else 0 end kvalificiral " +
            "            , case when best_lap-b.qualify_time<= 0 then null else best_lap-b.qualify_time end manjka " +
            "            , min(best_lap) over (partition by a.idSpeedCategory order by best_lap) najhitrejsi_voznik " +
            "            , DENSE_RANK() over (partition by idcontact order by a.idSpeedCategory desc) voznja_v_najhitrejsem_Razredu " +
            "             " +
            "             " +
            "        from " +
            "              (select idcontact,a.idtrack, idSpeedCategory, min(best_lap)best_lap, count(*)stevilo_vozenj, max(scheduled_at)visit_date  " +
            "                 from RF_Fact_Run_Results a " +
            "                    , RF_Ref_Track b  " +
            "                where a.idTrack =b.idTrack and b.active = 1  group by a.idtrack,idcontact, idSpeedCategory) a " +
            "             left join RF_Ref_Qualify_Times b on a.idSpeedCategory = b.idSpeedCategory and a.visit_date between b.datefrom and isnull(b.dateto,getdate()) and a.idtrack = b.idtrack " +
            "             left join (select distinct idSpeedCategory ,category_name  from RF_Ref_Speed) c on a.idSpeedCategory = c.idSpeedCategory  " +
            "       where a.idSpeedCategory> 300052)a " +
            "      join (select * from dwh_fact_contacts where contacttype = 'Starš') b on a.idcontact = b.idcontact " +
            "where voznja_v_najhitrejsem_Razredu = 1 " +
            ") a " +
            "left join  (select *, DENSE_RANK () over (partition by scheduled_at, idSpeedCategory, idTrack order by best_lap ) rank_that_day " +
            "              from (select idcontact, a.scheduled_at, b.idSpeedCategory, b.idTrack, min(b.best_lap) best_lap " +
            "                      from (select distinct cast(scheduled_at as date)scheduled_at " +
            "                              from  RF_Fact_Run_Results a " +
            "                                  , RF_Ref_Track b " +
            "                             where a.idTrack =b.idTrack and b.active = 1)a " +
            "                    left join (select a.* " +
            "                                 from RF_Fact_Run_Results a" +
            "                                    , RF_Ref_Track b " +
            "                                where a.idTrack =b.idTrack and b.active = 1 )b on a.scheduled_at >= cast(b.scheduled_at as date)  " +
            "                     group by idcontact, a.scheduled_at, b.idSpeedCategory, b.idTrack)a)b on cast(a.visit_date as date) = b.scheduled_at and a.idcontact = b.idcontact and a.idSpeedCategory = b.idSpeedCategory and a.idTrack = b.idTrack " +
            "left join ( select *, DENSE_RANK () over (partition by scheduled_at, idSpeedCategory,idTrack order by best_lap ) rank_today " +
            "              from (select idcontact, a.scheduled_at, b.idSpeedCategory, b.idTrack, min(b.best_lap) best_lap " +
            "                      from (select distinct cast(scheduled_at as date)scheduled_at  " +
            "                              from  RF_Fact_Run_Results a " +
            "                                  , RF_Ref_Track b  " +
            "                             where a.idTrack =b.idTrack and b.active = 1 )a " +
            "                             left join (select a.*  " +
            "                                          from RF_Fact_Run_Results a " +
            "                                             , RF_Ref_Track b  " +
            "                                         where a.idTrack =b.idTrack and b.active = 1  )b on cast(a.scheduled_at as date) >= cast(b.scheduled_at as date) " +
            "                     where cast(a.scheduled_at as date) = cast(DATEADD(day,-1, getdate())as date) " +
            "                  group by idcontact, a.scheduled_at, b.idSpeedCategory, b.idTrack)a)c on a.idcontact = c.idcontact and a.idSpeedCategory = c.idSpeedCategory and a.idTrack = c.idTrack               " +
            " where cast(visit_date as date) = dateadd(month,-1,cast(getdate() as date)) " +
            "               and dateadd(month,-1,dateadd(day,-1,cast(getdate() as date))) <> dateadd(month,-1,cast(getdate() as date)) " +
            "                and  dateadd(month,-1,dateadd(day,-2,cast(getdate() as date))) <> dateadd(month,-1,cast(getdate() as date)) " +
            "                and dateadd(month,-1,dateadd(day,-3,cast(getdate() as date))) <> dateadd(month,-1,cast(getdate() as date))  ", nativeQuery = true)
            List<Object[]> resultsForPastMonth();




    @Query(
            value = " select b.category_name, b.kvalificiral, b.Zaostanek_za_kvalifikacijo, za_prvim_zaostajas, b.contactemail, " +
                    "c.previous_best , c.yesterdays_best , \n" +
                    "case when c.previous_best - c.yesterdays_best > 0 then c.previous_best - c.yesterdays_best  else 0  end izboljsal,\n" +
                    "case when c.previous_best - c.yesterdays_best < 0 then abs(c.previous_best - c.yesterdays_best)  else 0  end poslabsal\n" +
                    ",e.rank_on_prev_date\n" +
                    ",g.rank_yesterday\n" +
                    " ,case when c.previous_best - c.yesterdays_best > 0 then c.yesterdays_best else isnull(c.previous_best,c.yesterdays_best) end all_time_best " +
                    " from (\n" +
                    "select  a.visit_date,a.idtrack,a.idcontact,a.idspeedcategory,a.category_name,a.kvalificiral,a.Zaostanek_za_kvalifikacijo ,a.za_prvim_zaostajas,a.contactemail,a.best_lap all_time_best  \n" +
                    "            from  \n" +
                    "            (select visit_date  \n" +
                    "            , a.idTrack  \n" +
                    "            , a.idcontact  \n" +
                    "            , idSpeedCategory  \n" +
                    "            , category_name  \n" +
                    "            , kvalificiral  \n" +
                    "            , case when kvalificiral <> 1  \n" +
                    "                   then best_lap-qualify_time   \n" +
                    "                   else null end Zaostanek_za_kvalifikacijo  \n" +
                    "                         , best_lap-najhitrejsi_voznik za_prvim_zaostajas  \n" +
                    "            , contactemail, best_lap  \n" +
                    "            from (select idcontact, category_name,a.idSpeedCategory,best_lap,qualify_time,visit_date,a.idTrack  \n" +
                    "                        , case when b.qualify_time > best_lap then 1 else 0 end kvalificiral  \n" +
                    "                        , case when best_lap-b.qualify_time<= 0 then null else best_lap-b.qualify_time end manjka  \n" +
                    "                        , min(best_lap) over (partition by a.idSpeedCategory order by best_lap) najhitrejsi_voznik  \n" +
                    "                        , DENSE_RANK() over (partition by idcontact order by a.idSpeedCategory desc) voznja_v_najhitrejsem_Razredu   \n" +
                    "                    from  \n" +
                    "                          (select idcontact,a.idtrack, idSpeedCategory, min(best_lap)best_lap, count(*)stevilo_vozenj, max(scheduled_at)visit_date   \n" +
                    "                             from RF_Fact_Run_Results a  \n" +
                    "                                , RF_Ref_Track b   \n" +
                    "                            where a.idTrack =b.idTrack and b.active = 1  group by a.idtrack,idcontact, idSpeedCategory) a  \n" +
                    "                         left join RF_Ref_Qualify_Times b on a.idSpeedCategory = b.idSpeedCategory and a.visit_date between b.datefrom and isnull(b.dateto,getdate()) and a.idtrack = b.idtrack  \n" +
                    "                         left join (select distinct idSpeedCategory ,category_name  from RF_Ref_Speed) c on a.idSpeedCategory = c.idSpeedCategory   \n" +
                    "                   where a.idSpeedCategory> 300052)a  \n" +
                    "                  join (select * from dwh_fact_contacts where contacttype = 'Starš') b on a.idcontact = b.idcontact  \n" +
                    "            where voznja_v_najhitrejsem_Razredu = 1  \n" +
                    "            ) a  \n" +
                    "             where cast(visit_date as date) = dateadd(day,-1,cast(getdate() as date))\n" +
                    "             ) b \n" +
                    "             left join (select min(case when cast(scheduled_at as date) < dateadd(day,-1,cast(getdate() as date)) then best_lap else null end) previous_best\n" +
                    "                             , min(case when cast(scheduled_at as date) = dateadd(day,-1,cast(getdate() as date)) then best_lap else null end) yesterdays_best\n" +
                    "                             , idcontact, idspeedcategory\n" +
                    "                         from RF_Fact_Run_Results a  \n" +
                    "                               , RF_Ref_Track b  \n" +
                    "                          where a.idTrack =b.idTrack and b.active = 1\n" +
                    "                         group by idcontact, idspeedcategory ) c on c.idcontact = b.idcontact and c.idspeedcategory = b.idspeedcategory                                       \n" +
                    "                         left join (select idcontact, best_lap , idspeedcategory , idtrack , cast(scheduled_at as date)scheduled_at  from ( select DENSE_RANK()  over (partition by idcontact, a.idtrack, idspeedcategory, cast(scheduled_at as date) order by best_lap asc,scheduled_at desc , idrun  ) rank1 , idcontact , best_lap, idspeedcategory , a.idtrack, scheduled_at from RF_Fact_Run_Results a,\n" +
                    "               RF_Ref_Track b  \n" +
                    "                                            where a.idTrack =b.idTrack and b.active = 1 )a \n" +
                    "              where rank1 = 1)d on d.idcontact = c.idcontact and d.idspeedcategory = c.idspeedcategory and d.best_lap =c.previous_best\n" +
                    "              left join  (select *, DENSE_RANK () over (partition by scheduled_at, idSpeedCategory, idTrack order by best_lap ) rank_on_prev_date \n" +
                    "                          from (select idcontact, a.scheduled_at, b.idSpeedCategory, b.idTrack, min(b.best_lap) best_lap  \n" +
                    "                                  from (select distinct cast(scheduled_at as date)scheduled_at  \n" +
                    "                                          from  RF_Fact_Run_Results a  \n" +
                    "                                              , RF_Ref_Track b  \n" +
                    "                                         where a.idTrack =b.idTrack and b.active = 1)a  \n" +
                    "                                left join (select a.*  \n" +
                    "                                             from RF_Fact_Run_Results a \n" +
                    "                                                , RF_Ref_Track b  \n" +
                    "                                            where a.idTrack =b.idTrack and b.active = 1 )b on a.scheduled_at >= cast(b.scheduled_at as date)   \n" +
                    "                                 group by idcontact, a.scheduled_at, b.idSpeedCategory, b.idTrack)a)e on d.scheduled_At = e.scheduled_at and d.idcontact = e.idcontact and d.idSpeedCategory = e.idSpeedCategory and d.idTrack = e.idTrack  \n" +
                    "                                 \n" +
                    "\t\t\t\tleft join  (select *, DENSE_RANK () over (partition by scheduled_at, idSpeedCategory, idTrack order by best_lap ) rank_yesterday \n" +
                    "                          from (select idcontact, a.scheduled_at, b.idSpeedCategory, b.idTrack, min(b.best_lap) best_lap  \n" +
                    "                                  from (select distinct cast(scheduled_at as date)scheduled_at  \n" +
                    "                                          from  RF_Fact_Run_Results a  \n" +
                    "                                              , RF_Ref_Track b  \n" +
                    "                                         where a.idTrack =b.idTrack and b.active = 1)a  \n" +
                    "                                left join (select a.*  \n" +
                    "                                             from RF_Fact_Run_Results a \n" +
                    "                                                , RF_Ref_Track b  \n" +
                    "                                            where a.idTrack =b.idTrack and b.active = 1 )b on a.scheduled_at >= cast(b.scheduled_at as date)   \n" +
                    "                                 group by idcontact, a.scheduled_at, b.idSpeedCategory, b.idTrack)a)g on cast(b.visit_date as date) = g.scheduled_at and b.idcontact = g.idcontact and b.idSpeedCategory = g.idSpeedCategory and b.idTrack = g.idTrack \n" +
                    "                                 " , nativeQuery = true    )
    List<Object[]> yesterdayResult();


    @Query(value =
            " select * from ( " +
                    "select " +
            " DENSE_RANK() over (partition by a.idSpeedCategory order by a.best_lap) position, a.* " +
            " from ( " +
            " select idcontact, idSpeedCategory , min(best_lap)best_lap from RF_Fact_Run_Results " +
            " where MONTH(cast(scheduled_at as date)) = :month " +
            "group by idcontact, idSpeedCategory " +
            "union all " +
            "select idcontact, idSpeedCategory , min(best_lap)best_lap   from RF_Fact_Run_Results where  idcontact = :idContact and idSpeedCategory = :category group by idcontact, idSpeedCategory  ) a " +
            "where idSpeedCategory = :category ) " +
                    " )a where idcontact = :idContact" , nativeQuery = true)
    List<Object[]> userPosition(@Param("month") Integer month, @Param("category") Integer category, @Param("idContact") Integer idContact );


    @Query(value =
    "        \n" +
            " select c.*,a.category_name from (     \n" +
            "  select  b.contact,\n" +
            "  case when len(b.email) < 1 then '/'\n" +
            "  \t\twhen b.email is null then '/'\n" +
            "  \t\telse b.email end email, max(a.category)maxcat \n" +
            "from (\n" +
            "  select \n" +
            "  a.idcontact, \n" +
            "  a.best_lap, d.qualify_time \n" +
            "  ,case when max_category = 300059 or (max_category = 300054 and a.best_lap < d.qualify_time) then 300059 \n" +
            "  \twhen max_category = 300054 or (max_category = 300053 and a.best_lap < d.qualify_time) then 300054\n" +
            "  \twhen max_category = 300053 or (max_category = 300052 and a.best_lap < d.qualify_time) then 300053\n" +
            "  \twhen max_category = 300052 or (max_category = 300051 and a.best_lap < d.qualify_time) then 300052\n" +
            "  \telse 300051 end category \n" +
            "  from (\n" +
            "      select idcontact , max(idspeedcategory) max_category, best_lap from RF_Fact_Run_Results \n" +
            "      group by idcontact, best_lap \n" +
            "\t)a \n" +
            "\tleft join RF_Fact_Run_Results c\n" +
            "\t\ton a.max_category = c.idSpeedCategory and a.idcontact = c.idcontact and a.best_lap = c.best_lap \n" +
            "\tleft join RF_Ref_Qualify_Times d \n" +
            "        \ton c.idSpeedCategory = d.idSpeedCategory and c.scheduled_at between d.datefrom and isnull(d.dateto,getdate()) and d.idtrack = c.idtrack\n" +
            "        \t) a \n" +
            "        \tleft join contact b \n" +
            "        \ton a.idcontact = b.idcontact\n" +
            "        group by b.contact, b.email \n" +
            ") c  \n" +
            "left join (select idspeedcategory, category_name from rf_ref_speed where query_relevant = 1) a\n" +
            "      on c.maxcat = a.idspeedcategory\n" +
            "      where contact like :name% " , nativeQuery = true )
        List<Object[]> listOfUsersMaxCategory(@Param("name") String name);


    @Query(value =
            "        \n" +
                    " select c.*,a.category_name from (     \n" +
                    "  select  b.contact,\n" +
                    "  case when len(b.email) < 1 then '/'\n" +
                    "  \t\twhen b.email is null then '/'\n" +
                    "  \t\telse b.email end email, max(a.category)maxcat \n" +
                    "from (\n" +
                    "  select \n" +
                    "  a.idcontact, \n" +
                    "  a.best_lap, d.qualify_time \n" +
                    "  ,case when max_category = 300059 or (max_category = 300054 and a.best_lap < d.qualify_time) then 300059 \n" +
                    "  \twhen max_category = 300054 or (max_category = 300053 and a.best_lap < d.qualify_time) then 300054\n" +
                    "  \twhen max_category = 300053 or (max_category = 300052 and a.best_lap < d.qualify_time) then 300053\n" +
                    "  \twhen max_category = 300052 or (max_category = 300051 and a.best_lap < d.qualify_time) then 300052\n" +
                    "  \telse 300051 end category \n" +
                    "  from (\n" +
                    "      select idcontact , max(idspeedcategory) max_category, best_lap from RF_Fact_Run_Results \n" +
                    "      group by idcontact, best_lap \n" +
                    "\t)a \n" +
                    "\tleft join RF_Fact_Run_Results c\n" +
                    "\t\ton a.max_category = c.idSpeedCategory and a.idcontact = c.idcontact and a.best_lap = c.best_lap \n" +
                    "\tleft join RF_Ref_Qualify_Times d \n" +
                    "        \ton c.idSpeedCategory = d.idSpeedCategory and c.scheduled_at between d.datefrom and isnull(d.dateto,getdate()) and d.idtrack = c.idtrack\n" +
                    "        \t) a \n" +
                    "        \tleft join contact b \n" +
                    "        \ton a.idcontact = b.idcontact\n" +
                    "        group by b.contact, b.email \n" +
                    ") c  \n" +
                    "left join (select idspeedcategory, category_name from rf_ref_speed where query_relevant = 1) a\n" +
                    "      on c.maxcat = a.idspeedcategory\n" +
                    "      where email like :email% " , nativeQuery = true )
    List<Object[]> listOfUsersMaxCategoryByEmail(@Param("email") String email);

}
