<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    <head>
        <link rel="stylesheet" href="css/reversi.css"/>
        <title>Reversi</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>

    <body>
        <form action="<c:url value='placeDisk'/>" method="POST">
            <table border="1">
                <tbody>
                    <tr>
                        <td class="header" colspan="2">
                            <input type="submit" value="Quit Game" name="quit"/>
                        </td>
                        <td class="header" colspan="4">
                            Scores <br>
                            Black: ${game.blackScore} White: ${game.whiteScore}
                        </td>
                        <td class="header" colspan="2">
                            <c:choose>
                                <c:when test="${game.turn % 2 == 0}">
                                    Player: Black 
                                </c:when> 
                                <c:otherwise>
                                    Player: White
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr><c:forEach var="cell" items="${game.board}" varStatus="loopPosition" >
                            <td class="${empty cell ? "empty" :  cell}">
                                <c:choose>
                                    <c:when test="${empty cell}">
                                        <input ${game.gameOver ? 'disabled' : ''} class="cell" type="submit" value="${loopPosition.index}" name="cellIndex" />
                                    </c:when>
                                    <c:otherwise> 
                                        ${cell}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <c:if test="${loopPosition.count % 8 == 0}">
                            </tr>
                            <tr>
                            </c:if>
                        </c:forEach>
                    </tr>
                    <tr class="footer">
                        <td colspan="2">
                            <input type="submit" value="Reset" name="reset"/>
                        </td>
                        <td class="messages" colspan="4">
                             <c:choose>
                                <c:when test="${game.gameOver}">
                                    <c:choose>
                                        <c:when test ="${game.blackScore > game.whiteScore}">
                                            Black Wins!
                                        </c:when>
                                        <c:when test = "${game.whiteScore > game.blackScore}">
                                            White Wins!
                                        </c:when>
                                        <c:otherwise>
                                            Game is a Draw.
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    ${errMsg}
                                </c:otherwise>   
                            </c:choose>
                        </td>
                        <td colspan="2">
                            <input ${game.gameOver ? 'disabled' : ''} type="submit" value="Pass" name="pass"/>
                        </td>
                    </tr>
                </tbody>
            </table>
        </form>
    </body>
</html>