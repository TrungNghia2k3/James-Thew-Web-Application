package com.ntn.culinary.service;

import com.ntn.culinary.dao.AnnounceWinnerDao;
import com.ntn.culinary.dao.AnnouncementDao;
import com.ntn.culinary.dao.ContestDao;
import com.ntn.culinary.dao.ContestEntryDao;
import com.ntn.culinary.exception.ConflictException;
import com.ntn.culinary.exception.NotFoundException;
import com.ntn.culinary.model.AnnounceWinner;
import com.ntn.culinary.model.Announcement;
import com.ntn.culinary.model.Contest;
import com.ntn.culinary.model.ContestEntry;
import com.ntn.culinary.request.AnnouncementRequest;
import com.ntn.culinary.response.AnnouncementResponse;
import com.ntn.culinary.service.impl.AnnouncementServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.ntn.culinary.fixture.TestDataFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnnouncementServiceImplTest {

    @Mock
    private ContestDao contestDao;

    @Mock
    private AnnouncementDao announcementDao;

    @Mock
    private AnnounceWinnerDao announceWinnerDao;

    @Mock
    private ContestEntryDao contestEntryDao;

    @InjectMocks
    private AnnouncementServiceImpl announcementService;

    @Test
    void getAllAnnouncements_WhenAnnouncementsExist_ReturnsAnnouncementResponses() {
        // Arrange
        Announcement announcement = createAnnouncement();
        Contest contest = createContest();
        AnnounceWinner winner = createWinner();
        ContestEntry entry = createContestEntry();

        when(announcementDao.getAllAnnouncements()).thenReturn(List.of(announcement));
        when(contestDao.getContestById(42)).thenReturn(contest);
        when(announceWinnerDao.getAllWinnersByAnnouncementId(1)).thenReturn(List.of(winner));
        when(contestEntryDao.getContestEntryById(200)).thenReturn(entry);

        // Act
        List<AnnouncementResponse> responses = announcementService.getAllAnnouncements();

        // Assert
        assertEquals(1, responses.size());
        AnnouncementResponse response = responses.getFirst();
        assertEquals("Test Announcement", response.getTitle());
        assertEquals("Contest Headline", response.getContest().getHeadline());
        assertEquals(1, response.getWinners().size());
        assertEquals("Entry Title", response.getWinners().getFirst().getContestEntry().getName());

        verify(announcementDao).getAllAnnouncements();
        verify(contestDao).getContestById(42);
        verify(announceWinnerDao).getAllWinnersByAnnouncementId(1);
        verify(contestEntryDao).getContestEntryById(200);
    }

    @Test
    void getAllAnnouncements_WhenNoAnnouncements_ReturnsEmptyList() {
        // Arrange
        when(announcementDao.getAllAnnouncements()).thenReturn(List.of());

        // Act
        List<AnnouncementResponse> responses = announcementService.getAllAnnouncements();

        // Assert
        assertNotNull(responses);
        assertTrue(responses.isEmpty());

        verify(announcementDao).getAllAnnouncements();
        verifyNoMoreInteractions(contestDao, announceWinnerDao, contestEntryDao);
    }

    @Test
    void getAllAnnouncements_WhenContestEntryNotFound_ThrowsRuntimeException() {
        // Arrange
        Announcement announcement = createAnnouncement();
        Contest contest = createContest();
        AnnounceWinner winner = createWinner();

        when(announcementDao.getAllAnnouncements()).thenReturn(List.of(announcement));
        when(contestDao.getContestById(42)).thenReturn(contest);
        when(announceWinnerDao.getAllWinnersByAnnouncementId(1)).thenReturn(List.of(winner));
        when(contestEntryDao.getContestEntryById(200)).thenThrow(new RuntimeException("ContestEntry not found"));

        // Act
        RuntimeException ex = assertThrows(RuntimeException.class, () -> announcementService.getAllAnnouncements());

        // Assert
        verify(announcementDao).getAllAnnouncements();
        verify(contestDao).getContestById(42);
        verify(announceWinnerDao).getAllWinnersByAnnouncementId(1);
        verify(contestEntryDao).getContestEntryById(200);
    }

    @Test
    void testAddAnnouncement_WhenValidRequest_ShouldInsertAnnouncementAndWinners() {
        // Arrange
        AnnouncementRequest request = createAnnouncementRequest();

        List<AnnounceWinner> winners = createWinnersList();
        request.setWinners(winners);

        // Mock contest tồn tại
        when(contestDao.existsById(1)).thenReturn(true);

        // Mock chưa có announcement cho contest này
        when(announcementDao.existsAnnouncementWithContest(1)).thenReturn(false);

        // Mock trả về announcementId
        when(announcementDao.getAnnouncementIdByContestId(1)).thenReturn(Optional.of(10));

        // Act
        announcementService.addAnnouncement(request);

        // Assert & Verify
        // Kiểm tra insertAnnouncement được gọi với Announcement phù hợp
        ArgumentCaptor<Announcement> annCaptor = ArgumentCaptor.forClass(Announcement.class);
        verify(announcementDao).insertAnnouncement(annCaptor.capture());

        Announcement inserted = annCaptor.getValue();
        assertEquals("New Announcement", inserted.getTitle());
        assertEquals("Description", inserted.getDescription());
        assertEquals(1, inserted.getContestId());
        assertEquals(2, inserted.getWinners().size());

        // Kiểm tra insertWinner được gọi 2 lần
        ArgumentCaptor<AnnounceWinner> winnerCaptor = ArgumentCaptor.forClass(AnnounceWinner.class);
        verify(announceWinnerDao, times(2)).insertWinner(winnerCaptor.capture());

        List<AnnounceWinner> insertedWinners = winnerCaptor.getAllValues();

        // Assert winner 1
        AnnounceWinner w1 = insertedWinners.getFirst();
        assertEquals(10, w1.getAnnouncementId());
        assertEquals(100, w1.getContestEntryId());
        assertEquals("1", w1.getRanking());

        // Assert winner 2
        AnnounceWinner w2 = insertedWinners.get(1);
        assertEquals(10, w2.getAnnouncementId());
        assertEquals(200, w2.getContestEntryId());
        assertEquals("2", w2.getRanking());
    }

    @Test
    void testAddAnnouncement_WhenContestDoesNotExist_ShouldThrowNotFoundException () {
        // Arrange
        AnnouncementRequest request = new AnnouncementRequest();
        request.setTitle("Title");
        request.setDescription("Description");
        request.setContestId(1);

        // Mock contest không tồn tại
        when(contestDao.existsById(1)).thenReturn(false);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> announcementService.addAnnouncement(request));

        // Verify không gọi insertAnnouncement
        verify(announcementDao, never()).insertAnnouncement(any());
    }

    @Test
    void addAnnouncement_WhenAnnouncementAlreadyExists_ShouldThrowConflictException() {
        // Arrange
        AnnouncementRequest request = createAnnouncementRequest();

        when(contestDao.existsById(1)).thenReturn(true);
        when(announcementDao.existsAnnouncementWithContest(1)).thenReturn(true);

        // Act & Assert
        assertThrows(ConflictException.class, () -> announcementService.addAnnouncement(request));

        verify(announcementDao, never()).insertAnnouncement(any());
    }

    @Test
    void addAnnouncement_WhenWinnersIsEmpty_ShouldHandleProperly() {
        // Arrange
        AnnouncementRequest request = new AnnouncementRequest();
        request.setTitle("Title");
        request.setDescription("Desc");
        request.setContestId(1);
        request.setWinners(Collections.emptyList());

        when(contestDao.existsById(1)).thenReturn(true);
        when(announcementDao.existsAnnouncementWithContest(1)).thenReturn(false);
        when(announcementDao.getAnnouncementIdByContestId(1)).thenReturn(Optional.of(10));

        // Act
        announcementService.addAnnouncement(request);

        // Assert
        verify(announcementDao).insertAnnouncement(any());
        verify(announceWinnerDao, never()).insertWinner(any());
    }

    @Test
    void addAnnouncement_WhenInsertWinnerFails_ShouldThrowRuntimeException() {
        // Arrange
        AnnouncementRequest request = new AnnouncementRequest();
        request.setTitle("Title");
        request.setDescription("Desc");
        request.setContestId(1);

        AnnounceWinner winner = new AnnounceWinner();
        winner.setContestEntryId(100);
        winner.setRanking("1");
        request.setWinners(List.of(winner));

        when(contestDao.existsById(1)).thenReturn(true);
        when(announcementDao.existsAnnouncementWithContest(1)).thenReturn(false);
        when(announcementDao.getAnnouncementIdByContestId(1)).thenReturn(Optional.of(10));

        // Giả sử insertWinner ném RuntimeException
        doThrow(new RuntimeException("DB error"))
                .when(announceWinnerDao).insertWinner(any());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            announcementService.addAnnouncement(request);
        });

        verify(announcementDao).insertAnnouncement(any());
        verify(announceWinnerDao).insertWinner(any());
    }

    // Test trường hợp contest không tồn tại
    @Test
    void testAddAnnouncement_ContestDoesNotExist() {
        // Arrange
        AnnouncementRequest request = new AnnouncementRequest();
        request.setTitle("Test");
        request.setDescription("Desc");
        request.setContestId(2);

        when(contestDao.existsById(2)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            announcementService.addAnnouncement(request);
        });

        assertTrue(ex.getMessage().contains("Contest with ID 2 does not exist."));

        verify(announcementDao, never()).insertAnnouncement(any());
        verify(announceWinnerDao, never()).insertWinner(any());
    }

    // Test trường hợp contest đã có announcement
    @Test
    void testAddAnnouncement_AnnouncementAlreadyExists() {
        // Arrange
        AnnouncementRequest request = new AnnouncementRequest();
        request.setTitle("Test");
        request.setDescription("Desc");
        request.setContestId(3);

        when(contestDao.existsById(3)).thenReturn(true);
        when(announcementDao.existsAnnouncementWithContest(3)).thenReturn(true);

        // Act + Assert
        RuntimeException ex = assertThrows(ConflictException.class, () -> {
            announcementService.addAnnouncement(request);
        });
        
        assertTrue(ex.getMessage().contains("Announcement already exists"));

        verify(announcementDao, never()).insertAnnouncement(any());
        verify(announceWinnerDao, never()).insertWinner(any());
    }

    @Test
    void testGetAllAnnouncements_WhenAnnouncementsExist_ReturnsAnnouncementResponses_() {
        // Arrange
        Announcement announcement = createAnnouncement();
        Contest contest = createContest();
        AnnounceWinner winner = createWinner();
        ContestEntry entry = createContestEntry();

//  Khi muốn dùng List nhiều phần tử ở trong.
//        List<Announcement> announcements = .createAnnouncementsList();
//        when(announcementDao.getAllAnnouncements()).thenReturn(announcements);

        when(announcementDao.getAllAnnouncements()).thenReturn(List.of(announcement));
        when(contestDao.getContestById(42)).thenReturn(contest);
        when(announceWinnerDao.getAllWinnersByAnnouncementId(1)).thenReturn(List.of(winner));
        when(contestEntryDao.getContestEntryById(200)).thenReturn(entry);


        // Act
        List<AnnouncementResponse> responses = announcementService.getAllAnnouncements();

        // Assert
        assertEquals(1, responses.size());
        AnnouncementResponse response = responses.getFirst();
        assertEquals("Test Announcement", response.getTitle());
        assertEquals("Contest Headline", response.getContest().getHeadline());
        assertEquals(1, response.getWinners().size());
        assertEquals("Entry Title", response.getWinners().getFirst().getContestEntry().getName());

        verify(announcementDao).getAllAnnouncements();
        verify(contestDao).getContestById(42);
        verify(announceWinnerDao).getAllWinnersByAnnouncementId(1);
        verify(contestEntryDao).getContestEntryById(200);
    }
}