package pl.lodz.p.library.adapters.soap.endpoints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.webservices.server.WebServiceServerTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringSource;
import pl.lodz.p.library.adapters.soap.converters.BookSetSoapConverter;
import pl.lodz.p.library.domain.model.BookSet;
import pl.lodz.p.library.ports.inbound.BookSetUseCase;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.noFault;
import static org.springframework.ws.test.server.ResponseMatchers.xpath;

@WebServiceServerTest
@Import({BookSetEndpoint.class, BookSetSoapConverter.class})
class BookSetEndpointTest {

    @Autowired
    private ApplicationContext context;

    @MockitoBean
    private BookSetUseCase bookSetUseCase;

    private MockWebServiceClient client;

    private static final String NS = "http://pl.lodz.p.library.adapters.soap.dto.bookset/";
    private static final Map<String, String> NS_MAP = Map.of("ns", NS);

    @BeforeEach
    void setUp() {
        client = MockWebServiceClient.createClient(context);
    }

    private BookSet buildBookSet(String id, String title, String author) {
        BookSet b = new BookSet(title, author, 2000, 5);
        b.setId(id);
        return b;
    }

    @Test
    void getAllBookSets_shouldReturnList() {
        when(bookSetUseCase.findAllBookSets())
                .thenReturn(List.of(buildBookSet("id-1", "Dune", "Herbert")));

        client.sendRequest(withPayload(new StringSource(
                "<GetAllBookSetsRequest xmlns=\"" + NS + "\"/>")))
                .andExpect(noFault())
                .andExpect(xpath("//ns:GetAllBookSetsResponse/ns:bookSet/ns:id", NS_MAP).evaluatesTo("id-1"))
                .andExpect(xpath("//ns:GetAllBookSetsResponse/ns:bookSet/ns:title", NS_MAP).evaluatesTo("Dune"));
    }

    @Test
    void getBookSetById_shouldReturnBookSet() {
        when(bookSetUseCase.findBookSetById("id-1"))
                .thenReturn(buildBookSet("id-1", "1984", "Orwell"));

        client.sendRequest(withPayload(new StringSource(
                "<GetBookSetByIdRequest xmlns=\"" + NS + "\"><id>id-1</id></GetBookSetByIdRequest>")))
                .andExpect(noFault())
                .andExpect(xpath("//ns:bookSet/ns:id", NS_MAP).evaluatesTo("id-1"))
                .andExpect(xpath("//ns:bookSet/ns:title", NS_MAP).evaluatesTo("1984"));
    }

    @Test
    void addBookSet_shouldReturnSavedBookSet() {
        when(bookSetUseCase.addBookSet(any(BookSet.class)))
                .thenReturn(buildBookSet("new-id", "Solaris", "Lem"));

        client.sendRequest(withPayload(new StringSource("""
                <AddBookSetRequest xmlns="%s">
                    <bookSetDTO>
                        <title>Solaris</title>
                        <author>Lem</author>
                        <releaseYear>1961</releaseYear>
                        <quantity>4</quantity>
                    </bookSetDTO>
                </AddBookSetRequest>""".formatted(NS))))
                .andExpect(noFault())
                .andExpect(xpath("//ns:bookSet/ns:id", NS_MAP).evaluatesTo("new-id"));
    }

    @Test
    void updateBookSet_shouldReturnUpdatedBookSet() {
        when(bookSetUseCase.updateBookSet(eq("id-upd"), any(BookSet.class)))
                .thenReturn(buildBookSet("id-upd", "Solaris PL", "Lem"));

        client.sendRequest(withPayload(new StringSource("""
                <UpdateBookSetRequest xmlns="%s">
                    <id>id-upd</id>
                    <bookSetDTO>
                        <title>Solaris PL</title>
                        <author>Lem</author>
                        <releaseYear>1961</releaseYear>
                        <quantity>10</quantity>
                    </bookSetDTO>
                </UpdateBookSetRequest>""".formatted(NS))))
                .andExpect(noFault())
                .andExpect(xpath("//ns:bookSet/ns:title", NS_MAP).evaluatesTo("Solaris PL"));
    }

    @Test
    void deleteBookSet_shouldReturnDeleted() {
        client.sendRequest(withPayload(new StringSource(
                "<DeleteBookSetRequest xmlns=\"" + NS + "\"><id>id-del</id></DeleteBookSetRequest>")))
                .andExpect(noFault())
                .andExpect(xpath("//ns:DeleteBookSetResponse/ns:isDeleted", NS_MAP).evaluatesTo("true"));

        verify(bookSetUseCase).deleteBookSet("id-del");
    }
}
