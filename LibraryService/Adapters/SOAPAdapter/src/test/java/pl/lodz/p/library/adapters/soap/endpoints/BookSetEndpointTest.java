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
import java.util.UUID;

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

    private static final String NS = "http://pl.lodz.p.library.adapters.soap.dto.bookset/";
    private static final Map<String, String> NS_MAP = Map.of("ns", NS);
    @Autowired
    private ApplicationContext context;
    @MockitoBean
    private BookSetUseCase bookSetUseCase;
    private MockWebServiceClient client;
    private UUID bs1Id;
    private UUID bs2Id;
    private UUID bsNewId;
    private UUID bsUpdId;
    private UUID bsDelId;

    @BeforeEach
    void setUp() {
        client = MockWebServiceClient.createClient(context);
        bs1Id = UUID.randomUUID();
        bs2Id = UUID.randomUUID();
        bsNewId = UUID.randomUUID();
        bsUpdId = UUID.randomUUID();
        bsDelId = UUID.randomUUID();
    }

    private BookSet buildBookSet(UUID id, String title, String author) {
        BookSet bookSet = new BookSet(title, author, 2000, 5);
        bookSet.setId(id);
        return bookSet;
    }

    @Test
    void getAllBookSets_shouldReturnList() {
        when(bookSetUseCase.findAllBookSets()).thenReturn(List.of(
                buildBookSet(bs1Id, "Title1", "Author1"),
                buildBookSet(bs2Id, "Title2", "Author2")
        ));

        client.sendRequest(withPayload(new StringSource(
                        "<GetAllBookSetsRequest xmlns=\"" + NS + "\"/>")))
                .andExpect(noFault())
                .andExpect(xpath("//ns:bookSet[1]/ns:id", NS_MAP).evaluatesTo(bs1Id.toString()))
                .andExpect(xpath("//ns:bookSet[2]/ns:title", NS_MAP).evaluatesTo("Title2"));
    }

    @Test
    void getBookSetById_shouldReturnBookSet() {
        when(bookSetUseCase.findBookSetById(bs1Id)).thenReturn(buildBookSet(bs1Id, "Title1", "Author1"));

        client.sendRequest(withPayload(new StringSource(
                        "<GetBookSetByIdRequest xmlns=\"" + NS + "\"><id>" + bs1Id + "</id></GetBookSetByIdRequest>")))
                .andExpect(noFault())
                .andExpect(xpath("//ns:bookSet/ns:author", NS_MAP).evaluatesTo("Author1"));
    }

    @Test
    void addBookSet_shouldReturnCreatedBookSet() {
        when(bookSetUseCase.addBookSet(any(BookSet.class))).thenReturn(buildBookSet(bsNewId, "Solaris", "Lem"));

        client.sendRequest(withPayload(new StringSource("""
                        <AddBookSetRequest xmlns="%s">
                            <bookSetDTO>
                                <title>Solaris</title>
                                <author>Lem</author>
                                <releaseYear>1961</releaseYear>
                                <quantity>10</quantity>
                            </bookSetDTO>
                        </AddBookSetRequest>""".formatted(NS))))
                .andExpect(noFault())
                .andExpect(xpath("//ns:bookSet/ns:id", NS_MAP).evaluatesTo(bsNewId.toString()));
    }

    @Test
    void updateBookSet_shouldReturnUpdatedBookSet() {
        when(bookSetUseCase.updateBookSet(eq(bsUpdId), any(BookSet.class)))
                .thenReturn(buildBookSet(bsUpdId, "Solaris PL", "Lem"));

        client.sendRequest(withPayload(new StringSource("""
                        <UpdateBookSetRequest xmlns="%s">
                            <id>%s</id>
                            <bookSetDTO>
                                <title>Solaris PL</title>
                                <author>Lem</author>
                                <releaseYear>1961</releaseYear>
                                <quantity>10</quantity>
                            </bookSetDTO>
                        </UpdateBookSetRequest>""".formatted(NS, bsUpdId.toString()))))
                .andExpect(noFault())
                .andExpect(xpath("//ns:bookSet/ns:title", NS_MAP).evaluatesTo("Solaris PL"));
    }

    @Test
    void deleteBookSet_shouldReturnDeleted() {
        client.sendRequest(withPayload(new StringSource(
                        "<DeleteBookSetRequest xmlns=\"" + NS + "\"><id>" + bsDelId + "</id></DeleteBookSetRequest>")))
                .andExpect(noFault())
                .andExpect(xpath("//ns:isDeleted", NS_MAP).evaluatesTo("true"));

        verify(bookSetUseCase).deleteBookSet(bsDelId);
    }
}