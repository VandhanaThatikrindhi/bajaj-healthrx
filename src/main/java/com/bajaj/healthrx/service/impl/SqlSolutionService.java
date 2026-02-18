package com.bajaj.healthrx.service.impl;

import com.bajaj.healthrx.service.HiringService;
import org.springframework.stereotype.Service;

@Service
public class SqlSolutionService implements HiringService {

    @Override
    public void executeFlow() {
        // This is intentionally left empty.
        // The actual orchestration is in TaskRunner.
        // This service provides SQL solutions via getSolution().
    }

    public String getSolution(boolean isOddQuestion) {

        // QUESTION 1 (Odd RegNo)
        if (isOddQuestion) {
            return """
                    SELECT
                        d.department_name AS DEPARTMENT_NAME,
                        t.total_salary AS SALARY,
                        CONCAT(e.first_name, ' ', e.last_name) AS EMPLOYEE_NAME,
                        TIMESTAMPDIFF(YEAR, e.dob, CURDATE()) AS AGE
                    FROM (
                        SELECT
                            e.emp_id,
                            e.department,
                            SUM(p.amount) AS total_salary,
                            ROW_NUMBER() OVER (
                                PARTITION BY e.department
                                ORDER BY SUM(p.amount) DESC
                            ) AS rn
                        FROM employee e
                        JOIN payments p ON e.emp_id = p.emp_id
                        WHERE DAY(p.payment_time) <> 1
                        GROUP BY e.emp_id, e.department
                    ) t
                    JOIN employee e ON t.emp_id = e.emp_id
                    JOIN department d ON e.department = d.department_id
                    WHERE t.rn = 1
                    """;
        }

        // QUESTION 2 (Even RegNo)
        return """
                SELECT
                    d.department_name AS DEPARTMENT_NAME,
                    AVG(TIMESTAMPDIFF(YEAR, e.dob, CURDATE())) AS AVERAGE_AGE,
                    GROUP_CONCAT(DISTINCT CONCAT(e.first_name, ' ', e.last_name)
                                 ORDER BY e.emp_id
                                 SEPARATOR ', ') AS EMPLOYEE_LIST
                FROM employee e
                JOIN payments p ON e.emp_id = p.emp_id
                JOIN department d ON e.department = d.department_id
                WHERE p.amount > 70000
                GROUP BY d.department_id, d.department_name
                ORDER BY d.department_id DESC
                """;
    }
}
