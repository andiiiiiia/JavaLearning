1. 什么是DB？

    https://www.oracle.com/cn/database/what-is-database/

    结构化信息或数据的有序集合

   由数据库管理系统 (DBMS) 来控制

   数据、DBMS 及关联应用一起被称为数据库系统，通常简称为数据库

3. 什么是DBMS?
    
    数据库管理系统。数据库与其用户或程序之间的接口，允许用户检索、更新和管理信息的组织和优化方式。此外，DBMS 还有助于监督和控制数据库，提供各种管理操作，例如性能监视、调优、备份和恢复。

    常用DBMS:关系型：Oracle,Mysql,pgSQl,sqlServer,gaussDB。

    NoSQL(非关系型):mongo,redis（键值，灵活schema，高拓展）,Hbase(大数据),elasticSearch...
    
3. DB种类：
    关系型数据库，分布式数据库(gaussDB)，NoSQL数据库（mongo,redis,elasticSearch...），等等
4. gaussDB的特点
   分布式架构，适用于OLTP和OLAP，PB级，存算分离，高可用，跨AZ（available zone）部署...